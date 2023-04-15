package khantorecrm.service.impl;

import khantorecrm.model.Delivery;
import khantorecrm.model.DeliveryMovingProductHistory;
import khantorecrm.model.Input;
import khantorecrm.model.ItemForCollection;
import khantorecrm.model.Output;
import khantorecrm.model.ProductItem;
import khantorecrm.model.ProductPricesForSellers;
import khantorecrm.model.User;
import khantorecrm.model.enums.ActionType;
import khantorecrm.model.enums.OutputType;
import khantorecrm.model.enums.ProductType;
import khantorecrm.model.enums.RoleName;
import khantorecrm.payload.dao.OwnResponse;
import khantorecrm.payload.dao.projection.ProductPriceForSellerProjection;
import khantorecrm.payload.dto.DeliveryDto;
import khantorecrm.payload.dto.DeliveryShareDto;
import khantorecrm.payload.dto.ProductPriceForSellerDto;
import khantorecrm.payload.dto.ReturnProductDto;
import khantorecrm.repository.DeliveryMovingProductHistoryRepository;
import khantorecrm.repository.DeliveryRepository;
import khantorecrm.repository.InputRepository;
import khantorecrm.repository.OutputRepository;
import khantorecrm.repository.ProductItemRepository;
import khantorecrm.repository.ProductPriceForSellersRepository;
import khantorecrm.service.IDeliveryService;
import khantorecrm.service.functionality.Creatable;
import khantorecrm.service.functionality.Deletable;
import khantorecrm.service.functionality.InstanceReturnable;
import khantorecrm.utils.exceptions.NotFoundException;
import khantorecrm.utils.exceptions.TypesInError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static khantorecrm.utils.constants.Statics.getCurrentUser;
import static khantorecrm.utils.constants.Statics.isNonDeletable;

@Service
public class DeliveryService implements
        InstanceReturnable<Delivery, Long>,
        Creatable<DeliveryDto>,
        Deletable<Long>,
        IDeliveryService {
    private final DeliveryRepository repository;
    private final ProductItemRepository itemRepository;
    private final InputRepository inputRepository;
    private final OutputRepository outputRepository;
    private final DeliveryMovingProductHistoryRepository movingProductHistoryRepository;
    private final ProductPriceForSellersRepository priceForSellersRepository;

    @Autowired
    public DeliveryService(
            DeliveryRepository repository,
            ProductItemRepository itemRepository,
            InputRepository inputRepository,
            OutputRepository outputRepository,
            DeliveryMovingProductHistoryRepository movingProductHistoryRepository,
            ProductPriceForSellersRepository priceForSellersRepository
    ) {
        this.repository = repository;
        this.itemRepository = itemRepository;
        this.inputRepository = inputRepository;
        this.outputRepository = outputRepository;
        this.movingProductHistoryRepository = movingProductHistoryRepository;
        this.priceForSellersRepository = priceForSellersRepository;
    }

    @Override
    public List<Delivery> getAllInstances() {
        return repository.findAll();
    }

    @Override
    public Delivery getInstanceWithId(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    @Transactional()
    public OwnResponse create(DeliveryDto dto) {
        try {
            Delivery delivery = repository.findById(dto.getDeliveryId()).orElseThrow(
                    () -> new NotFoundException("Delivery with id " + dto.getDeliveryId() + " already exists")
            );

            final Set<ItemForCollection> orderedItems = dto.getItems().stream().map(
                    itemDto -> {
                        ProductItem productItem = itemRepository.findById(itemDto.getProductItemId()).orElseThrow(
                                () -> new NotFoundException("Product item with id " + itemDto.getProductItemId() + " not found")
                        );

                        // check product item amount enough delivery amount
//                        if (productItem.getItemAmount() < itemDto.getAmount())
//                            throw new TypesInError("Not enough items in stock");

                        // delivery baggage items
                        List<ProductItem> allByWarehouseId = itemRepository.findAllByWarehouseId(delivery.getBaggage().getId());

                        // save changes of product item
                        productItem.setItemAmount(productItem.getItemAmount() - itemDto.getAmount());

                        Optional<ProductItem> doHave = allByWarehouseId.stream().filter(
                                item -> item.getItemProduct().getId().equals(productItem.getItemProduct().getId())).findFirst();

                        if (doHave.isPresent()) {
                            ProductItem baggageItem = doHave.get();
                            baggageItem.setItemAmount(baggageItem.getItemAmount() + itemDto.getAmount());
                            return new ItemForCollection(
                                    baggageItem,
                                    itemDto.getAmount(),
                                    baggageItem.getItemProduct().getPrice(),
                                    baggageItem.getItemProduct().getIngredients().stream().mapToDouble(it -> it.getProductItem().getItemProduct().getPrice()).sum()
                            );
                        } else {
                            return new ItemForCollection(
                                    new ProductItem(
                                            productItem.getItemProduct(),
                                            itemDto.getAmount(),
                                            delivery.getBaggage()
                                    ),
                                    itemDto.getAmount(),
                                    productItem.getItemProduct().getPrice(),
                                    productItem.getItemProduct().getIngredients().stream().mapToDouble(it -> it.getProductItem().getItemProduct().getPrice()).sum()
                            );
                        }
                    }).collect(Collectors.toSet());

            // creating output, output may be changed
            outputRepository.save(
                    new Output(
                            orderedItems,
                            OutputType.DELIVERY,
                            delivery
                    )
            );
            return OwnResponse.CREATED_SUCCESSFULLY;
        } catch (NotFoundException e) {
            return OwnResponse.DELIVERY_NOT_FOUND.setMessage(e.getMessage());
        } catch (TypesInError e) {
            return OwnResponse.INPUT_TYPE_ERROR.setMessage(e.getMessage());
        } catch (RuntimeException e) {
            return OwnResponse.ERROR.setMessage(e.getMessage());
        }
    }

    @Override
    public OwnResponse getBaggageWithUserId(Long id) {
        try {
            final Delivery delivery = repository.findById(id).orElseThrow(
                    () -> new NotFoundException("Delivery with user id " + id + " not found")
            );
            return OwnResponse.ALL_DATA.setData(
                    itemRepository.findAllBaggageItemByDeliveryWarehouseId(
                            delivery.getBaggage().getId(),
                            delivery.getId()
                    ).stream().filter(it -> it.getProductAmount() > 0).collect(Collectors.toList())
            );
        } catch (NotFoundException e) {
            return OwnResponse.DELIVERY_NOT_FOUND.setMessage(e.getMessage());
        } catch (Exception e) {
            return OwnResponse.ALL_DATA.setData(new int[]{});
        }
    }

    @Override
    public OwnResponse returnSelectedProduct(ReturnProductDto dto) {
        try {
            // if user is deliverer
            Delivery delivery = repository.findByDeliverer_Id(((User) (SecurityContextHolder.getContext().getAuthentication().getPrincipal())).getId()).orElseThrow(
                    () -> new NotFoundException("You aren't a deliverer")
            );

            List<ProductItem> allByWarehouseId = itemRepository.findAllByWarehouseId(delivery.getBaggage().getId());

            // if returns product exists in the database
            ProductItem realBaggageItem = itemRepository.findById(dto.getReturnedProductItemId()).orElseThrow(
                    () -> new NotFoundException("Product item with id " + dto.getReturnedProductItemId() + " not found in the baggage !")
            );

            // if recipient product exists in the database
            final ProductItem realWarehouseItem = itemRepository.findAllByWarehouseId(dto.getRecipientWarehouseId())
                    .stream()
                    .filter(item -> item.getItemProduct().getId().equals(realBaggageItem.getItemProduct().getId()))
                    .findFirst()
                    .orElseThrow(
                            () -> new NotFoundException("Product item with id " + dto.getReturnedProductItemId() + " not found in the recipient warehouse !")
                    );

            // check if product is in the baggage
            if (allByWarehouseId.stream().noneMatch(
                    item -> Objects.equals(dto.getReturnedProductItemId(), item.getId()))
            )
                throw new NotFoundException("Product item with " + dto.getReturnedProductItemId() + " not found in the baggage !");

            // amount should be less than real product amount
            if (realBaggageItem.getItemAmount() < dto.getAmount())
                throw new TypesInError("Amount should be less than product amount");

            realBaggageItem.setItemAmount(realBaggageItem.getItemAmount() - dto.getAmount());

            inputRepository.save(
                    new Input(
                            realWarehouseItem,
                            dto.getAmount(),
                            ProductType.PRODUCT,
                            realWarehouseItem.getItemProduct().getPrice(),
                            null,
                            0.0,
                            ActionType.WAIT
                    )
            );
            return OwnResponse.ACTION_PROCESS_IS_WAITING.setMessage("Return process is waiting");
        } catch (NotFoundException e) {
            return OwnResponse.DELIVERY_NOT_FOUND.setMessage(e.getMessage());
        }
    }

    @Override
    public OwnResponse acceptReturnedProduct(Long inputId) {
        try {
            Input input = inputRepository.findById(inputId).orElseThrow(
                    () -> new NotFoundException("Input with id " + inputId + " not found !")
            );

            if (input.getStatus().equals(ActionType.ACCEPTED))
                throw new TypesInError("The input is already accepted");

            ProductItem productItem = input.getProductItem();

            // add returning amount
            productItem.setItemAmount(productItem.getItemAmount() + input.getAmount());

            // change input status
            input.setStatus(ActionType.ACCEPTED);

            // product item should be merged
            inputRepository.save(input);

            return OwnResponse.UPDATED_SUCCESSFULLY;
        } catch (NotFoundException e) {
            return OwnResponse.NOT_FOUND.setMessage(e.getMessage());
        } catch (TypesInError e) {
            return OwnResponse.INPUT_TYPE_ERROR.setMessage(e.getMessage());
        }
    }

    @Override
    public OwnResponse rejectReturnedProduct(Long inputId) {
        try {
            Input input = inputRepository.findById(inputId).orElseThrow(
                    () -> new NotFoundException("Input with id " + inputId + " not found !")
            );

            if (input.getStatus().equals(ActionType.ACCEPTED))
                throw new TypesInError("The input is already accepted");

            final Delivery delivery = repository.findById(
                    input.getCreatedBy().getId()
            ).orElseThrow(
                    () -> new NotFoundException("Delivery with id " + input.getCreatedBy().getId() + " not found !")
            );

            ProductItem productItem = itemRepository.findAllByWarehouseId(delivery.getBaggage().getId()).stream().filter(
                    item -> Objects.equals(item.getItemProduct().getId(), input.getProductItem().getItemProduct().getId())
            ).findAny().orElseThrow(
                    () -> new NotFoundException("Product item with id " + input.getProductItem().getId() + " not found in the baggage !")
            );

            productItem.setItemAmount(productItem.getItemAmount() + input.getAmount());
            itemRepository.save(productItem);
            inputRepository.delete(input);

            return OwnResponse.REJECTED_SUCCESSFULLY;
        } catch (NotFoundException e) {
            return OwnResponse.NOT_FOUND.setMessage(e.getMessage());
        } catch (TypesInError e) {
            return OwnResponse.INPUT_TYPE_ERROR.setMessage(e.getMessage());
        } catch (Exception e) {
            return OwnResponse.ERROR.setMessage("error with input delete");
        }
    }

    @Override
    public List<Output> getAllOrders() {
        return outputRepository.findAllByType(OutputType.DELIVERY);
    }

    @Override
    public List<Input> getAllWaitReturnsWithId(Long id) {
        return inputRepository.findAllByStatusAndCreatedBy_Id(ActionType.WAIT, id);
    }

    @Override
    public List<Output> getAllOrdersByDriverId(Long id) {
        return outputRepository.findAllByDelivery_Id(id);
    }

    @Override
    public List<Input> getAllWaitReturns() {
        return inputRepository.findAllByStatusAndCreatedBy_Role_RoleName(ActionType.WAIT, RoleName.DRIVER);
    }

    @Override
    public OwnResponse shareWithDriver(DeliveryShareDto dto) {
        try {
            // current user should be a driver
            // get current driver
            final Delivery currentDeliverer = repository.findById(
                    ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId()
            ).orElseThrow(
                    () -> new NotFoundException("You aren't a driver")
            );

            final Delivery secondDeliverer = repository.findById(dto.getDeliveryId()).orElseThrow(
                    () -> new NotFoundException("Delivery with id " + dto.getDeliveryId() + " not found !")
            );

            // check if the driver is not the same
            if (currentDeliverer.getId().equals(secondDeliverer.getId()))
                throw new TypesInError("You can't share with yourself");

            // get moving product
            final ProductItem movingProduct = itemRepository.findById(dto.getProductItemId()).orElseThrow(
                    () -> new NotFoundException("Product with id " + dto.getProductItemId() + " not found !")
            );

            // check if the product is in the current driver's baggage
            if (!movingProduct.getWarehouse().getId().equals(currentDeliverer.getBaggage().getId()))
                throw new NotFoundException("Product with id " + dto.getProductItemId() + " not found in your baggage !");

            // check if the product amount is less than the moving amount
            if (movingProduct.getItemAmount() < dto.getAmount())
                throw new TypesInError("Amount should be less than product amount");

            // saving current product amount
            movingProduct.setItemAmount(movingProduct.getItemAmount() - dto.getAmount());
            itemRepository.save(movingProduct);

            // saving moving product
            movingProductHistoryRepository.save(
                    new DeliveryMovingProductHistory(
                            currentDeliverer,
                            secondDeliverer,
                            movingProduct.getItemProduct(),
                            dto.getAmount(),
                            ActionType.WAIT
                    )
            );

            return OwnResponse.UPDATED_SUCCESSFULLY;
        } catch (NotFoundException e) {
            return OwnResponse.NOT_FOUND.setMessage(e.getMessage());
        } catch (TypesInError e) {
            return OwnResponse.INPUT_TYPE_ERROR.setMessage(e.getMessage());
        }
    }

    @Override
    public OwnResponse acceptMovingProductWithDeliverer(Long movingId) {
        try {
            final DeliveryMovingProductHistory deliveryMovingProductHistoryWithId = movingProductHistoryRepository.findById(movingId).orElseThrow(
                    () -> new NotFoundException("Moving product with id " + movingId + " not found !")
            );

            // check if the moving product is not accepted
            if (deliveryMovingProductHistoryWithId.getAction().equals(ActionType.ACCEPTED))
                throw new TypesInError("The moving product is already accepted");

            if (
                    !deliveryMovingProductHistoryWithId.getToDelivery().getId().equals(
                            ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId())
            ) return OwnResponse.NOT_FOUND.setMessage("You aren't the receiver of this product");

            // get second driver's baggage
            final List<ProductItem> secondDriverBaggage = itemRepository.findAllByWarehouseId(deliveryMovingProductHistoryWithId.getToDelivery().getBaggage().getId());

            // check if the product is in the second driver's baggage
            if (
                    secondDriverBaggage.stream().noneMatch(
                            item -> Objects.equals(item.getItemProduct().getId(), deliveryMovingProductHistoryWithId.getProduct().getId()))
            ) {
                // create new product item
                itemRepository.save(
                        new ProductItem(
                                deliveryMovingProductHistoryWithId.getProduct(),
                                deliveryMovingProductHistoryWithId.getAmount(),
                                deliveryMovingProductHistoryWithId.getToDelivery().getBaggage()
                        )
                );
            } else {
                // get the product item
                final ProductItem secondDriverProductItem = secondDriverBaggage.stream().filter(
                        item -> Objects.equals(item.getItemProduct().getId(), deliveryMovingProductHistoryWithId.getProduct().getId())
                ).findFirst().orElseThrow(
                        () -> new NotFoundException("Product with id " + deliveryMovingProductHistoryWithId.getProduct().getId() + " not found in the second driver's baggage !")
                );

                // add amount
                secondDriverProductItem.setItemAmount(secondDriverProductItem.getItemAmount() + deliveryMovingProductHistoryWithId.getAmount());
                itemRepository.save(secondDriverProductItem);
            }
                deliveryMovingProductHistoryWithId.setAction(ActionType.ACCEPTED);
                movingProductHistoryRepository.save(deliveryMovingProductHistoryWithId);
        } catch (NotFoundException e) {
            return OwnResponse.NOT_FOUND.setMessage(e.getMessage());
        } catch (TypesInError e) {
            return OwnResponse.INPUT_TYPE_ERROR.setMessage(e.getMessage());
        }
        return OwnResponse.UPDATED_SUCCESSFULLY.setData("Product moved successfully !");
    }

    @Override
    public OwnResponse rejectMovingProductWithDeliverer(Long movingId) {
        try {
            final DeliveryMovingProductHistory deliveryMovingProductHistoryWithId = movingProductHistoryRepository.findById(movingId).orElseThrow(
                    () -> new NotFoundException("Moving product with id " + movingId + " not found !")
            );

            // check if the moving product is not accepted
            if (!deliveryMovingProductHistoryWithId.getAction().equals(ActionType.WAIT))
                throw new TypesInError("The moving product is already accepted or rejected");

            if (
                    !deliveryMovingProductHistoryWithId.getToDelivery().getId().equals(
                            ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId())
            ) return OwnResponse.NOT_FOUND.setMessage("You aren't the receiver of this product");

            // get current driver's baggage
            final List<ProductItem> currentDriverBaggage = itemRepository.findAllByWarehouseId(deliveryMovingProductHistoryWithId.getFromDelivery().getBaggage().getId());

            // check if the product is in the current driver's baggage
            if (
                    currentDriverBaggage.stream().noneMatch(
                            item -> Objects.equals(item.getItemProduct().getId(), deliveryMovingProductHistoryWithId.getProduct().getId()))
            ) {
                // create new product item
                itemRepository.save(
                        new ProductItem(
                                deliveryMovingProductHistoryWithId.getProduct(),
                                deliveryMovingProductHistoryWithId.getAmount(),
                                deliveryMovingProductHistoryWithId.getFromDelivery().getBaggage()
                        )
                );
            } else {
                // get the product item
                final ProductItem currentDriverProductItem = currentDriverBaggage.stream().filter(
                        item -> Objects.equals(item.getItemProduct().getId(), deliveryMovingProductHistoryWithId.getProduct().getId())
                ).findFirst().orElseThrow(
                        () -> new NotFoundException("Product with id " + deliveryMovingProductHistoryWithId.getProduct().getId() + " not found in the current driver's baggage !")
                );

                // add amount
                currentDriverProductItem.setItemAmount(currentDriverProductItem.getItemAmount() + deliveryMovingProductHistoryWithId.getAmount());
                itemRepository.save(currentDriverProductItem);
            }

            deliveryMovingProductHistoryWithId.setAction(ActionType.REJECTED);
            movingProductHistoryRepository.save(deliveryMovingProductHistoryWithId);
            return OwnResponse.UPDATED_SUCCESSFULLY.setData("Product moving rejected successfully !");
        } catch (NotFoundException e) {
            return OwnResponse.NOT_FOUND.setMessage(e.getMessage());
        } catch (TypesInError e) {
            return OwnResponse.INPUT_TYPE_ERROR.setMessage(e.getMessage());
        }
    }

    @Override
    public List<DeliveryMovingProductHistory> getAllMovingWithDelivererId(Long id) {
        return movingProductHistoryRepository.findAllByToDeliveryId(id, Sort.by(Sort.Direction.DESC, "id"));
    }

    @Override
    public OwnResponse productPriceInjecting(ProductPriceForSellerDto dto) {
        try {
            if (!repository.existsById(dto.getDelivererId())) {
                throw new NotFoundException("Delivery not found");
            }
            final List<ProductPricesForSellers> collect = dto.getProductList()
                    .stream()
                    .map(
                            item -> priceForSellersRepository.findByDelivererIdAndProductId(dto.getDelivererId(), item.getProductId()).orElse(
                                    new ProductPricesForSellers(
                                            dto.getDelivererId(),
                                            item.getProductId(),
                                            item.getAmount()
                                    )
                            ).setPrice(item.getAmount())
                    ).collect(Collectors.toList());
            priceForSellersRepository.saveAll(collect);
            return OwnResponse.CREATED_SUCCESSFULLY;
        } catch (NotFoundException e) {
            return OwnResponse.NOT_FOUND.setMessage(e.getMessage());
        } catch (RuntimeException e) {
            return OwnResponse.INPUT_TYPE_ERROR.setMessage("There is something wrong");
        }
    }

    @Override
    public List<ProductPriceForSellerProjection> productPricesByDelivererId(Long id) {
        return priceForSellersRepository.getAllByDelivererId(id);
    }

    @Override
    public OwnResponse delete(Long id) {
        try {
            final User currentUser = getCurrentUser();

            final Output output = outputRepository.findById(id).orElseThrow(
                    () -> new NotFoundException("Output not found")
            );

            if (!currentUser.getRole().getRoleName().equals(RoleName.ADMIN) && isNonDeletable(output.getCreatedAt().getTime())) {
                return OwnResponse.CANT_DELETE;
            }
            outputRepository.deleteById(id);

//            rollbackProductItems(output);
            return OwnResponse.DELETED_SUCCESSFULLY;
        } catch (NotFoundException e) {
            return OwnResponse.NOT_FOUND.setMessage(e.getMessage());
        } catch (RuntimeException e) {
            return OwnResponse.INPUT_TYPE_ERROR.setMessage("Can't delete this output");
        } catch (Exception e) {
            return OwnResponse.CANT_DELETE.setMessage("Cant delete this output");
        }
    }
//
//    private void rollbackProductItems(Output output) {
//        output.getProductItems().forEach(
//                item -> {
//                    final ProductItem productItem = itemRepository.findById(item.getId()).orElseThrow(
//                            () -> new NotFoundException("Product item not found")
//                    );
//                    productItem.setItemAmount(productItem.getItemAmount() + item.getItemAmount());
//                    itemRepository.save(productItem);
//                }
//        );
//    }
}
