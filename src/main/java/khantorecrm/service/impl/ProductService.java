package khantorecrm.service.impl;

import khantorecrm.model.Ingredient;
import khantorecrm.model.Product;
import khantorecrm.model.enums.ProductType;
import khantorecrm.payload.dao.OwnResponse;
import khantorecrm.payload.dto.ProductDto;
import khantorecrm.repository.IngredientRepository;
import khantorecrm.repository.ProductItemRepository;
import khantorecrm.repository.ProductRepository;
import khantorecrm.service.functionality.Creatable;
import khantorecrm.service.functionality.InstanceReturnable;
import khantorecrm.service.functionality.Updatable;
import khantorecrm.utils.exceptions.ProductNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService implements
        InstanceReturnable<Product, Long>,
        Creatable<ProductDto>,
        Updatable<ProductDto, Long> {

    private final ProductRepository repository;
    private final IngredientRepository ingredientRepository;
    private final ProductItemRepository productItemRepository;

    @Autowired
    public ProductService(ProductRepository repository, IngredientRepository ingredientRepository, ProductItemRepository productItemRepository) {
        this.repository = repository;
        this.ingredientRepository = ingredientRepository;
        this.productItemRepository = productItemRepository;
    }

    @Override
    public List<Product> getAllInstances() {
        return repository.findAll();
    }

    @Override
    public Product getInstanceWithId(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public OwnResponse create(ProductDto dto) {
        if (repository.existsByName(dto.getName())) return OwnResponse.PRODUCT_ALREADY_EXISTS;
        Product product = dto.toEntity();
        try {
            if (product.getType() == ProductType.PRODUCT) {
                if (dto.getIngredients() == null) return OwnResponse.INGREDIENTS_NOT_FOUND;
                product.setIngredients(
                        dto.getIngredients().stream().map(
                                ingredient -> {
                                    Ingredient ingredientProduct = new Ingredient();
                                    ingredientProduct.setProductItem(
                                            productItemRepository.findById(
                                                    ingredient.getProductItemId()
                                            ).orElseThrow(
                                                    () ->  new ProductNotFoundException("Product item with id " + ingredient.getProductItemId() + " not found")
                                            )
                                    );
                                    ingredientProduct.setItemAmount(ingredient.getAmount());
                                    ingredientProduct.setProduct(product);
                                    return ingredientProduct;
                                }
                        ).collect(Collectors.toSet())
                );
            }
            repository.save(product);
        } catch (ProductNotFoundException e) {
            return OwnResponse.INGREDIENTS_NOT_FOUND.setData(e.getMessage());
        } catch (Exception e) {
            return OwnResponse.ERROR;
        }


        return OwnResponse.CREATED_SUCCESSFULLY;
    }

    @Override
    public OwnResponse update(ProductDto dto, Long id) {
        if (repository.existsByNameAndIdIsNot(dto.getName(), id)) return OwnResponse.PRODUCT_ALREADY_EXISTS;
        return repository.findById(id).map(
                product -> {
                    product.setName(dto.getName());
                    product.setPrice(dto.getPrice());
                    repository.save(product);
                    return OwnResponse.UPDATED_SUCCESSFULLY;
                }).orElse(OwnResponse.PRODUCT_NOT_FOUND);
    }

    // todo: add delete method
}
