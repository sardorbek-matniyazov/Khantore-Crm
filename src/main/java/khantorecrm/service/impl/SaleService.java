package khantorecrm.service.impl;

import khantorecrm.model.Balance;
import khantorecrm.model.Client;
import khantorecrm.model.ItemForCollection;
import khantorecrm.model.Output;
import khantorecrm.model.Payment;
import khantorecrm.model.ProductItem;
import khantorecrm.model.Sale;
import khantorecrm.model.enums.OutputType;
import khantorecrm.model.enums.PaymentType;
import khantorecrm.model.enums.ProductType;
import khantorecrm.payload.dao.OwnResponse;
import khantorecrm.payload.dto.ProductItemListDto;
import khantorecrm.payload.dto.SaleDto;
import khantorecrm.repository.ClientRepository;
import khantorecrm.repository.ProductItemRepository;
import khantorecrm.repository.SaleRepository;
import khantorecrm.service.ISaleService;
import khantorecrm.service.functionality.InstanceReturnable;
import khantorecrm.utils.exceptions.NotFoundException;
import khantorecrm.utils.exceptions.TypesInError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
public class SaleService
        implements InstanceReturnable<Sale, Long>,
        ISaleService {
    private final SaleRepository repository;
    private final ProductItemRepository itemRepository;
    private final ClientRepository clientRepository;

    @Autowired
    public SaleService(SaleRepository repository, ProductItemRepository itemRepository, ClientRepository clientRepository) {
        this.repository = repository;
        this.itemRepository = itemRepository;
        this.clientRepository = clientRepository;
    }

    @Override
    public List<Sale> getAllInstances() {
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
            AtomicReference<Double> atomicWholePrice = new AtomicReference<>(0.0);
            Set<ItemForCollection> collect = dto.getProductItemsList().stream().filter(ProductItemListDto::isItemCreatableId).map(
                    dItem -> {
                        ProductItem item = itemRepository.findById(
                                dItem.getProductItemId()).orElseThrow(
                                () -> new NotFoundException("Product item with id " + dItem.getProductItemId() + " not found")
                        );

                        if (item.getItemProduct().getType().equals(ProductType.INGREDIENT))
                            throw new TypesInError("Product item with id " + dItem.getProductItemId() + " is ingredient");

                        if (item.getItemAmount() < dItem.getAmount())
                            throw new TypesInError("There aren't enough product in the warehouse !");

                        atomicWholePrice.updateAndGet(v -> (v + item.getItemProduct().getPrice() * dItem.getAmount()));

                        item.setItemAmount(item.getItemAmount() - dItem.getAmount());

                        return new ItemForCollection(
                                item,
                                dItem.getAmount(),
                                item.getItemProduct().getPrice(),
                                item.getItemProduct().getIngredients().stream().mapToDouble(it -> it.getProductItem().getItemProduct().getPrice()).sum()
                        );
                    }
            ).collect(Collectors.toSet());

            Client client = clientRepository.findById(dto.getClientId()).orElseThrow(
                    () -> new NotFoundException("Client with id " + dto.getClientId() + " not found")
            );

            final Double wholePrice = atomicWholePrice.get();
            final double debtPrice = wholePrice - dto.getPaymentAmount();
            if (debtPrice < 0)
                throw new TypesInError("Sale Whole price should be less than payment price");

            // changing balance of client
            final Balance balance = client.getBalance();
            balance.setAmount(balance.getAmount() - debtPrice);

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
                            new Payment(
                                    dto.getPaymentAmount(),
                                    PaymentType.CASH
                            )
                    )
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
}
