package khantorecrm.service.impl;

import khantorecrm.model.Product;
import khantorecrm.model.ProductItem;
import khantorecrm.model.Warehouse;
import khantorecrm.model.WarehouseMovingProductHistory;
import khantorecrm.model.enums.ProductType;
import khantorecrm.payload.dao.OwnResponse;
import khantorecrm.payload.dto.MovingItemDto;
import khantorecrm.payload.dto.ProductList;
import khantorecrm.payload.dto.WarehouseDto;
import khantorecrm.repository.MovingWarehouseHistoryRepository;
import khantorecrm.repository.ProductItemRepository;
import khantorecrm.repository.ProductRepository;
import khantorecrm.repository.WarehouseRepository;
import khantorecrm.service.IWarehouseService;
import khantorecrm.service.functionality.Creatable;
import khantorecrm.service.functionality.InstanceReturnable;
import khantorecrm.service.functionality.Updatable;
import khantorecrm.utils.exceptions.ProductAlreadyInTheWarehouseException;
import khantorecrm.utils.exceptions.NotFoundException;
import khantorecrm.utils.exceptions.ProductsNotEqualException;
import khantorecrm.utils.exceptions.TypesInError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class WarehouseService implements
        InstanceReturnable<Warehouse, Long>,
        Creatable<WarehouseDto>,
        Updatable<WarehouseDto, Long>,
        IWarehouseService {

    private final WarehouseRepository repository;
    private final ProductItemRepository itemRepository;
    private final ProductRepository productRepository;
    private final MovingWarehouseHistoryRepository warehouseHistoryRepository;

    @Autowired
    public WarehouseService(
            WarehouseRepository repository,
            ProductItemRepository itemRepository,
            ProductRepository productRepository,
            MovingWarehouseHistoryRepository warehouseHistoryRepository) {
        this.repository = repository;
        this.itemRepository = itemRepository;
        this.productRepository = productRepository;
        this.warehouseHistoryRepository = warehouseHistoryRepository;
    }

    @Override
    public List<Warehouse> getAllInstances() {
        return repository.findAll();
    }

    @Override
    public Warehouse getInstanceWithId(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public OwnResponse create(WarehouseDto dto) {
        if (repository.existsByName(dto.getName())) {
            return OwnResponse.WAREHOUSE_ALREADY_EXISTS;
        }
        Warehouse warehouse = new Warehouse(dto.getName(), dto.getType());
        try {
            Map<Long, Character> uniqueIds = new HashMap<>();
            itemRepository.saveAll(dto.getProductList().stream().map(productList -> {
                // some DSA idea
                if (uniqueIds.containsKey(productList.getProductId()))
                    throw new TypesInError("Each item should be unique");

                uniqueIds.put(productList.getProductId(), 'a');

                ProductItem productItem = new ProductItem(productRepository.findById(productList.getProductId()).orElseThrow(() -> new NotFoundException("Product with id " + productList.getProductId() + " not found")), productList.getAmount());
                if (!productItem.getItemProduct().getType().equals(warehouse.getType())) {
                    throw new TypesInError("Product with id " + productList.getProductId() + " type " + productItem.getItemProduct().getType() + " not equal with warehouse type " + warehouse.getType());
                }
                productItem.setWarehouse(warehouse);
                return productItem;
            }).collect(Collectors.toSet()));
            repository.save(warehouse);
        } catch (NotFoundException e) {
            return OwnResponse.PRODUCT_NOT_FOUND.setMessage(e.getMessage());
        } catch (TypesInError e) {
            return OwnResponse.WAREHOUSE_ITEMS_TYPE_NOT_EQUAL.setMessage(e.getMessage());
        } catch (Exception e) {
            return OwnResponse.ERROR.setMessage(e.getMessage());
        }

        return OwnResponse.CREATED_SUCCESSFULLY;
    }

    @Override
    public OwnResponse update(WarehouseDto dto, Long id) {
        return null;
    }

    @Override
    public List<ProductItem> getAllItemsWithWarehouseId(Long id) {
        return itemRepository.findAllByWarehouseId(id);
    }

    @Override
    public OwnResponse addItemToWarehouse(Long id, ProductList dto) {
        Optional<Warehouse> byId = repository.findById(id);
        try {
            byId.ifPresent(warehouse -> {
                if (itemRepository.existsByWarehouseIdAndItemProduct_Id(id, dto.getProductId())) {
                    throw new ProductAlreadyInTheWarehouseException("The product Already in the warehouse");
                }
                Product product = productRepository.findById(dto.getProductId()).orElseThrow(
                        () -> new NotFoundException("Product with id " + dto.getProductId() + " not found in the database !")
                );
                if (!product.getType().equals(warehouse.getType())) throw new TypesInError("Warehouse type should equal product type");

                itemRepository.save(
                        new ProductItem(
                                product,
                                dto.getAmount(),
                                warehouse
                        )
                );
            });
        } catch (ProductAlreadyInTheWarehouseException e) {
            return OwnResponse.PRODUCT_ALREADY_EXISTS_IN_THE_WAREHOUSE.setMessage(e.getMessage());
        } catch (NotFoundException e) {
            return OwnResponse.PRODUCT_NOT_FOUND.setMessage(e.getMessage());
        } catch (TypesInError e) {
            return OwnResponse.TYPES_NOT_EQUAL.setMessage(e.getMessage());
        } catch (Exception e) {
            return OwnResponse.ERROR.setMessage(e.getMessage());
        }
        return OwnResponse.UPDATED_SUCCESSFULLY;
    }

    @Override
    public OwnResponse moveItem(MovingItemDto dto) {
        try {
            if (dto.getWarehouseOneId().equals(dto.getWarehouseTwoId())) {
                return OwnResponse.INPUT_TYPE_ERROR.setMessage("Warehouse one and two should be different");
            }
            final ProductItem itemOne = itemRepository.findAllByWarehouseId(dto.getWarehouseOneId())
                    .stream()
                    .filter(item -> item.getItemProduct().getId().equals(dto.getProductId()))
                    .findFirst()
                    .orElseThrow(
                            () -> new NotFoundException("Product doesn't exists in the Warehouse one")
                    );

            final ProductItem itemTwo = itemRepository.findAllByWarehouseId(dto.getWarehouseTwoId())
                    .stream()
                    .filter(item -> item.getItemProduct().getId().equals(dto.getProductId()))
                    .findFirst()
                    .orElseThrow(
                            () -> new NotFoundException("Product doesn't exists in the Warehouse two")
                    );

            if (dto.getAmount() > itemOne.getItemAmount()) {
                return OwnResponse.INPUT_TYPE_ERROR.setMessage("Amount should be less than item amount");
            }

            itemOne.setItemAmount(itemOne.getItemAmount() - dto.getAmount());
            itemTwo.setItemAmount(itemTwo.getItemAmount() + dto.getAmount());

            // repository.saveAll(List.of(itemOne.getWarehouse(), itemTwo.getWarehouse()));
            itemRepository.save(itemOne);
            itemRepository.save(itemTwo);

            // saving history of moving
            warehouseHistoryRepository.save(
                    new WarehouseMovingProductHistory(
                            itemOne.getItemProduct(),
                            itemOne.getWarehouse(),
                            itemTwo.getWarehouse(),
                            dto.getAmount()
                    )
            );

            return OwnResponse.UPDATED_SUCCESSFULLY;

        } catch (NotFoundException e) {
            return OwnResponse.PRODUCT_NOT_FOUND.setMessage(e.getMessage());
        } catch (ProductsNotEqualException e) {
            return OwnResponse.PRODUCT_NOT_EQUAL.setMessage(e.getMessage());
        } catch (Exception e) {
            return OwnResponse.ERROR.setMessage(e.getMessage());
        }
    }

    @Override
    public List<ProductItem> getAllItemsByType(ProductType type) {
        return itemRepository.findAllByItemProduct_TypeAndWarehouse_Type(type, type);
    }

    @Override
    public List<Warehouse> getAllByType(ProductType type) {
        return repository.findAllByType(type);
    }
}
