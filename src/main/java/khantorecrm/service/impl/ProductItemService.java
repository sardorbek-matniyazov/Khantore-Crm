package khantorecrm.service.impl;

import khantorecrm.model.ProductItem;
import khantorecrm.model.enums.ProductType;
import khantorecrm.repository.ProductItemRepository;
import khantorecrm.service.IProductItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductItemService implements IProductItemService {
    private final ProductItemRepository repository;

    @Autowired
    public ProductItemService(ProductItemRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<ProductItem> getAllWarningIngredients() {
        return repository.findAllByItemProduct_TypeAndWarehouse_Type(ProductType.INGREDIENT, ProductType.INGREDIENT)
                .stream()
                .filter(productItem -> productItem.getItemAmount() < productItem.getItemProduct().getWarningAmount())
                .collect(Collectors.toList());
    }
}
