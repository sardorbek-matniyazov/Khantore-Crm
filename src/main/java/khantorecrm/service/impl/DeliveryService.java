package khantorecrm.service.impl;

import khantorecrm.model.*;
import khantorecrm.model.enums.ActionType;
import khantorecrm.model.enums.OutputType;
import khantorecrm.model.enums.ProductType;
import khantorecrm.payload.dao.OwnResponse;
import khantorecrm.payload.dto.DeliveryDto;
import khantorecrm.payload.dto.ReturnProductDto;
import khantorecrm.repository.*;
import khantorecrm.service.IDeliveryService;
import khantorecrm.service.functionality.Creatable;
import khantorecrm.service.functionality.InstanceReturnable;
import khantorecrm.utils.exceptions.NotFoundException;
import khantorecrm.utils.exceptions.TypesInError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
public class DeliveryService implements
        InstanceReturnable<Delivery, Long>,
        Creatable<DeliveryDto>,
        IDeliveryService {
    private final DeliveryRepository repository;
    private final ProductItemRepository itemRepository;
    private final InputRepository inputRepository;
    private final UserRepository userRepository;
    private final OutputRepository outputRepository;

    @Autowired
    public DeliveryService(DeliveryRepository repository, ProductItemRepository itemRepository, InputRepository inputRepository, UserRepository userRepository, OutputRepository outputRepository) {
        this.repository = repository;
        this.itemRepository = itemRepository;
        this.inputRepository = inputRepository;
        this.userRepository = userRepository;
        this.outputRepository = outputRepository;
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

            ProductItem productItem = itemRepository.findById(dto.getProductItemId()).orElseThrow(
                    () -> new NotFoundException("Product item with id " + dto.getProductItemId() + " not found")
            );

            // check product item amount enough delivery amount
            if (productItem.getItemAmount() < dto.getAmount())
                throw new TypesInError("Not enough items in stock");

            // delivery baggage items
            List<ProductItem> allByWarehouseId = itemRepository.findAllByWarehouseId(delivery.getBaggage().getId());

            Optional<ProductItem> doHave = allByWarehouseId.stream().filter(item -> item.getItemProduct().getId().equals(productItem.getItemProduct().getId())).findFirst();

            if (doHave.isPresent()) {
                ProductItem baggageItem = doHave.get();
                baggageItem.setItemAmount(baggageItem.getItemAmount() + dto.getAmount());
                itemRepository.save(baggageItem);
            } else {
                itemRepository.save(
                        new ProductItem(
                                productItem.getItemProduct(),
                                dto.getAmount(),
                                delivery.getBaggage()
                        )
                );
            }

            // save changes of product item
            productItem.setItemAmount(productItem.getItemAmount() - dto.getAmount());
            itemRepository.save(productItem);

            // creating output, output may be changed
            outputRepository.save(
                    new Output(
                            new HashSet<>(
                                    Collections.singletonList(
                                            new ItemForCollection(
                                                    productItem,
                                                    dto.getAmount(),
                                                    productItem.getItemProduct().getPrice()
                                            )
                                    )
                            ),
                            OutputType.DELIVERY,
                            delivery
                    )
            );
            return OwnResponse.CREATED_SUCCESSFULLY;
        } catch (NotFoundException e) {
            return OwnResponse.DELIVERY_NOT_FOUND.setMessage(e.getMessage());
        } catch (RuntimeException e) {
            return OwnResponse.ERROR.setMessage(e.getMessage());
        }
    }

    @Override
    public OwnResponse getBaggageWithDeliveryId(Long id) {
        // if user is deliverer
        try {
            return OwnResponse.ALL_DATA.setData(
                    itemRepository.findAllByWarehouseId(
                            repository.findById(id).orElseThrow(
                                    () -> new NotFoundException("Delivery with id " + id + " Not found !")
                            ).getBaggage().getId()
                    )
            );
        } catch (NotFoundException e) {
            return OwnResponse.DELIVERY_NOT_FOUND.setMessage(e.getMessage());
        }
    }

    @Override
    public OwnResponse returnSelectedProduct(ReturnProductDto dto) {

        try {
            // if user is deliverer
            Delivery delivery = repository.findById(((User) (SecurityContextHolder.getContext().getAuthentication().getPrincipal())).getId()).orElseThrow(
                    () -> new NotFoundException("You aren't a deliverer")
            );

            List<ProductItem> allByWarehouseId = itemRepository.findAllByWarehouseId(delivery.getBaggage().getId());

            // if returns product exists in the database
            ProductItem realBaggageItem = itemRepository.findById(dto.getReturnedProductItemId()).orElseThrow(
                    () -> new NotFoundException("Product item with id " + dto.getReturnedProductItemId() + " not found in the baggage !")
            );

            // if recipient product exists in the database
            final Optional<ProductItem> first = itemRepository.findAllByWarehouseId(dto.getRecipientWarehouseId()).stream()
                    .filter(item -> item.getItemProduct().getId().equals(realBaggageItem.getItemProduct().getId()))
                    .findFirst();
            ProductItem realWarehouseItem;
            if (first.isPresent()) {
                realWarehouseItem = first.get();
            } else
                throw new NotFoundException("Product item with id " + dto.getReturnedProductItemId() + " not found in the recipient warehouse !");

            // check if product is in the baggage
            if (allByWarehouseId.stream().noneMatch(
                    item -> Objects.equals(dto.getReturnedProductItemId(), item.getId()))
            ) throw new NotFoundException("Product item with " + dto.getReturnedProductItemId() + " not found in the baggage !");

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
                    userRepository.findById(
                            input.getCreatedBy().getId()
                    ).orElseThrow(
                            () -> new NotFoundException("User with id " + input.getCreatedBy().getId() + " not found !")
                    ).getId()
            ).orElseThrow(
                    () -> new NotFoundException("Delivery with id " + input.getCreatedBy().getId() + " not found !")
            );

            ProductItem productItem = itemRepository.findAllByWarehouseId(delivery.getBaggage().getId()).stream().filter(
                    item -> Objects.equals(item.getId(), input.getProductItem().getId())
            ).findFirst().orElseThrow(
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
        return outputRepository.findAllByCreatedBy_Id(id);
    }
}
