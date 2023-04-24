package khantorecrm.service.impl;

import khantorecrm.model.Balance;
import khantorecrm.model.Client;
import khantorecrm.model.ItemForCollection;
import khantorecrm.model.Output;
import khantorecrm.model.Payment;
import khantorecrm.model.ProductItem;
import khantorecrm.model.Sale;
import khantorecrm.model.User;
import khantorecrm.model.enums.OutputType;
import khantorecrm.model.enums.PaymentOrderType;
import khantorecrm.model.enums.PaymentStatus;
import khantorecrm.model.enums.PaymentType;
import khantorecrm.model.enums.ProductType;
import khantorecrm.model.enums.RoleName;
import khantorecrm.payload.dao.OwnResponse;
import khantorecrm.payload.dto.PaymentConfirmDto;
import khantorecrm.payload.dto.ProductItemListDto;
import khantorecrm.payload.dto.SaleDto;
import khantorecrm.repository.BalanceRepository;
import khantorecrm.repository.ClientRepository;
import khantorecrm.repository.PaymentRepository;
import khantorecrm.repository.ProductItemRepository;
import khantorecrm.repository.ProductPriceForSellersRepository;
import khantorecrm.repository.SaleRepository;
import khantorecrm.service.IClientSmsService;
import khantorecrm.service.ISaleService;
import khantorecrm.service.functionality.Deletable;
import khantorecrm.service.functionality.InstanceReturnable;
import khantorecrm.utils.exceptions.NotFoundException;
import khantorecrm.utils.exceptions.TypesInError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static khantorecrm.utils.constants.Statics.getCurrentUser;
import static khantorecrm.utils.constants.Statics.getTime;
import static khantorecrm.utils.constants.Statics.isNonDeletable;

@Service
public class SaleService
        implements InstanceReturnable<Sale, Long>,
        Deletable<Long>,
        ISaleService {
    private final SaleRepository repository;
    private final ProductItemRepository itemRepository;
    private final ClientRepository clientRepository;
    private final BalanceRepository balanceRepository;
    private final ProductPriceForSellersRepository priceForSellersRepository;
    private final PaymentRepository paymentRepository;
    private final IClientSmsService clientSmsService;

    @Autowired
    public SaleService(
            SaleRepository repository, ProductItemRepository itemRepository,
            ClientRepository clientRepository, BalanceRepository balanceRepository,
            ProductPriceForSellersRepository priceForSellersRepository,
            PaymentRepository paymentRepository, IClientSmsService clientSmsService) {
        this.repository = repository;
        this.itemRepository = itemRepository;
        this.clientRepository = clientRepository;
        this.balanceRepository = balanceRepository;
        this.priceForSellersRepository = priceForSellersRepository;
        this.paymentRepository = paymentRepository;
        this.clientSmsService = clientSmsService;
    }

    @Override
    public List<Sale> getAllInstances() {
        final User currentUser = getCurrentUser();
        if (!currentUser.getRole().getRoleName().equals(RoleName.ADMIN))
            return repository.findAllByCreatedBy_Id(currentUser.getId(), Sort.by(Sort.Direction.DESC, "id"));
        return repository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    @Override
    public Sale getInstanceWithId(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public OwnResponse sell(SaleDto dto) {
        try {
            final User crUser = getCurrentUser();
            AtomicReference<Double> atomicWholePrice = new AtomicReference<>(0.0);
            Set<ItemForCollection> collect = dto.getProductItemsList().stream().filter(ProductItemListDto::isItemCreatableId).map(
                    dItem -> {
                        if (dItem.getAmount() == null) throw new TypesInError("Item amount shouldn't be null");

                        ProductItem item = itemRepository.findById(
                                dItem.getProductItemId()).orElseThrow(
                                () -> new NotFoundException("Product item with id " + dItem.getProductItemId() + " not found")
                        );

                        if (item.getItemProduct().getType().equals(ProductType.INGREDIENT))
                            throw new TypesInError("Product item with id " + dItem.getProductItemId() + " is ingredient");

//                        if (item.getItemAmount() < dItem.getAmount())
//                            throw new TypesInError("There aren't enough product in the warehouse !");

                        final Double drPrice;
                        if (Objects.requireNonNull(crUser.getRole().getRoleName()) == RoleName.DRIVER) {
                            drPrice = priceForSellersRepository.findByDelivererIdAndProductId(crUser.getId(), item.getItemProduct().getId())
                                    .orElseThrow(
                                            () -> new NotFoundException(String.format("Product %s price nor set", item.getItemProduct().getName()))
                                    )
                                    .getPrice();
                        } else {
                            drPrice = item.getItemProduct().getPrice();
                        }

                        atomicWholePrice.updateAndGet(v -> (v + drPrice * dItem.getAmount()));

                        item.setItemAmount(item.getItemAmount() - dItem.getAmount());

                        return new ItemForCollection(
                                item,
                                dItem.getAmount(),
                                drPrice,
                                item.getItemProduct().getIngredients().stream().mapToDouble(it -> it.getItemAmount() * it.getProductItem().getItemProduct().getPrice()).sum()
                        );
                    }
            ).collect(Collectors.toSet());

            Client client = clientRepository.findById(dto.getClientId()).orElseThrow(
                    () -> new NotFoundException("Client with id " + dto.getClientId() + " not found")
            );

            final Double wholePrice = atomicWholePrice.get();
            final Double debtPrice = wholePrice - dto.getPaymentAmount();
            if (debtPrice < 0)
                throw new TypesInError("Sale Whole price should be less than payment price");

            // changing balance of client
            final Balance balance = client.getBalance();
            balance.setAmount(balance.getAmount() - debtPrice);

            // creating payment
            final Payment savedPayment = paymentRepository.save(
                    new Payment(
                            dto.getPaymentAmount(),
                            PaymentType.CASH,
                            PaymentStatus.INCOME
                    )
            );

            // adding payment to client
            client.getBalance().getPayments().add(savedPayment);

            // creating sale
            repository.save(
                    new Sale(
                            new Output(
                                    collect,
                                    OutputType.SALE
                            ),
                            client,
                            wholePrice,
                            debtPrice,
                            savedPayment,
                            getCurrentUser().getKpiPercent()
                    )
            );

            // increase balance
            increaseUsersAmountWithKpi(wholePrice);

            // send sms to client
            if (client.getPhone() != null && !client.getPhone().isEmpty())
                clientSmsService.sendSmsAfterEachSale(
                        client.getName(),
                        client.getPhone(),
                        wholePrice.toString(),
                        ((Double)(wholePrice - debtPrice)).toString(),
                        debtPrice.toString()
                );

            return OwnResponse.CREATED_SUCCESSFULLY;
        } catch (NotFoundException e) {
            return OwnResponse.PRODUCT_NOT_FOUND.setMessage(e.getMessage());
        } catch (TypesInError e) {
            return OwnResponse.INPUT_TYPE_ERROR.setMessage(e.getMessage());
        } catch (Exception e) {
            return OwnResponse.ERROR.setMessage(e.getMessage());
        }
    }

    @Override
    public List<Sale> getAllInstancesByClientName(Long clientId) {
        return repository.findAllByClientId(clientId, Sort.by(Sort.Direction.DESC, "id"));
    }

    @Override
    @Transactional
    public OwnResponse confirmPayment(PaymentConfirmDto confirmDto) {
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, -100);
            final Integer aLong = paymentRepository.updatePaymentsByPeriod(
                    confirmDto.getCreatedById(),
                    getTime(confirmDto.getStartDate()),
                    getTime(confirmDto.getEndDate()),
                    PaymentOrderType.NEW.name()
            );
            System.out.println(aLong);
        } catch (TypesInError e) {
            return OwnResponse.INPUT_TYPE_ERROR.setMessage(e.getMessage());
        } catch (Exception e) {
            return OwnResponse.ERROR.setMessage(e.getMessage());
        }
        return OwnResponse.UPDATED_SUCCESSFULLY.setMessage("Qabillandi");
    }

    @Override
    public List<Sale> getPaymentSumsByPeriod(Long createdById, String startDate, String endDate) {
        return repository.selectPaymentsByPeriod(createdById, getTime(startDate), getTime(endDate));
    }

    public void increaseUsersAmountWithKpi(Double amount) {
        final User currentUser = getCurrentUser();

        if (currentUser.getKpiPercent() == 0)
            return;
        final Balance balance = currentUser.getBalance();
        balance.setAmount(balance.getAmount() + amount * currentUser.getKpiPercent() / 100.0D);

        balanceRepository.save(balance);
    }

    @Override
    public OwnResponse delete(Long id) {
        try {
            final Sale sale = repository.findById(id).orElseThrow(
                    () -> new NotFoundException("Sale with id " + id + " not found")
            );

            final User currentUser = getCurrentUser();
            if (!currentUser.getRole().getRoleName().equals(RoleName.ADMIN) && (isNonDeletable(sale.getCreatedAt().getTime()) && !sale.getCreatedBy().getId().equals(currentUser.getId()))) {
                return OwnResponse.CANT_DELETE.setMessage("Can't delete");
            }
            final Double wholePrice = sale.getWholePrice();
            final Double debtPrice = sale.getDebtPrice();
            if (!Objects.equals(debtPrice, wholePrice)) {
                final Balance balance = sale.getClient().getBalance();

                balance.setAmount(balance.getAmount() + (wholePrice - debtPrice));
                balanceRepository.save(balance);
            }

            // rollback kpi
            rollbackKpi(sale);

            // turn back products to warehouse
            rollbackProductItems(sale.getOutput().getProductItems());

            repository.delete(sale);
            return OwnResponse.DELETED_SUCCESSFULLY.setMessage("Sale deleted successfully");
        } catch (NotFoundException e) {
            return OwnResponse.NOT_FOUND.setMessage(e.getMessage());
        } catch (Exception e) {
            return OwnResponse.CANT_DELETE.setMessage(e.getMessage());
        }
    }

    private void rollbackProductItems(Set<ItemForCollection> productItems) {
        productItems.forEach(
                item -> {
                    final ProductItem productItem = item.getProductItem();
                    productItem.setItemAmount(productItem.getItemAmount() + item.getItemAmount());
                    itemRepository.save(productItem);
                }
        );
    }

    private void rollbackKpi(Sale sale) {
        final User createdBy = sale.getCreatedBy();
        if (createdBy.getKpiPercent() == 0)
            return;
        final Balance balance = createdBy.getBalance();
        balance.setAmount(balance.getAmount() - sale.getWholePrice() * createdBy.getKpiPercent() / 100.0D);

        balanceRepository.save(balance);
    }
}
