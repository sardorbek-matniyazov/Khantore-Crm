package khantorecrm.service.impl;

import khantorecrm.model.ProductItem;
import khantorecrm.model.Warehouse;
import khantorecrm.payload.dao.OwnResponse;
import khantorecrm.payload.dto.ProductList;
import khantorecrm.payload.dto.WarehouseDto;
import khantorecrm.repository.ProductItemRepository;
import khantorecrm.repository.ProductRepository;
import khantorecrm.repository.WarehouseRepository;
import khantorecrm.service.IWarehouseService;
import khantorecrm.service.functionality.Creatable;
import khantorecrm.service.functionality.InstanceReturnable;
import khantorecrm.service.functionality.Updatable;
import khantorecrm.utils.exceptions.ProductAlreadyInTheWarehouseException;
import khantorecrm.utils.exceptions.ProductNotFoundException;
import khantorecrm.utils.exceptions.WarehouseItemsTypeNotEqual;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class WarehouseService implements InstanceReturnable<Warehouse, Long>, Creatable<WarehouseDto>, Updatable<WarehouseDto, Long>, IWarehouseService {

    private final WarehouseRepository repository;
    private final ProductItemRepository itemRepository;
    private final ProductRepository productRepository;

    @Autowired
    public WarehouseService(WarehouseRepository repository, ProductItemRepository itemRepository, ProductRepository productRepository) {
        this.repository = repository;
        this.itemRepository = itemRepository;
        this.productRepository = productRepository;
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
            itemRepository.saveAll(dto.getProductList().stream().map(productList -> {
                ProductItem productItem = new ProductItem(productRepository.findById(productList.getProductId()).orElseThrow(() -> new ProductNotFoundException("Product with id " + productList.getProductId() + " not found")), productList.getAmount());
                if (!productItem.getItemProduct().getType().equals(warehouse.getType())) {
                    throw new WarehouseItemsTypeNotEqual("Product with id " + productList.getProductId() + " type " + productItem.getItemProduct().getType() + " not equal with warehouse type " + warehouse.getType());
                }
                productItem.setWarehouse(warehouse);
                return productItem;
            }).collect(Collectors.toSet()));
            repository.save(warehouse);
        } catch (ProductNotFoundException e) {
            return OwnResponse.PRODUCT_NOT_FOUND.setMessage(e.getMessage());
        } catch (WarehouseItemsTypeNotEqual e) {
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
            byId.ifPresent(it -> {
                if (itemRepository.existsByWarehouseId(id)) {
                    throw new ProductAlreadyInTheWarehouseException("The product Already in the warehouse");
                }
            });
        } catch (ProductNotFoundException e) {
            return OwnResponse.PRODUCT_ALREADY_EXISTS_IN_THE_WAREHOUSE.setMessage(e.getMessage());
        }
        return OwnResponse.UPDATED_SUCCESSFULLY;
    }
}
