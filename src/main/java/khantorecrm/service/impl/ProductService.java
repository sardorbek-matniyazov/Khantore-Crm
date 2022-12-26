package khantorecrm.service.impl;

import khantorecrm.model.ItemForCollection;
import khantorecrm.model.Product;
import khantorecrm.model.enums.ProductType;
import khantorecrm.payload.dao.OwnResponse;
import khantorecrm.payload.dto.ProductDto;
import khantorecrm.payload.dto.ProductItemListDto;
import khantorecrm.repository.IngredientRepository;
import khantorecrm.repository.ProductItemRepository;
import khantorecrm.repository.ProductRepository;
import khantorecrm.service.IProductService;
import khantorecrm.service.functionality.Creatable;
import khantorecrm.service.functionality.InstanceReturnable;
import khantorecrm.service.functionality.Updatable;
import khantorecrm.utils.exceptions.NotFoundException;
import khantorecrm.utils.exceptions.TypesInError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProductService implements
        InstanceReturnable<Product, Long>,
        Creatable<ProductDto>,
        Updatable<ProductDto, Long>,
        IProductService {

    private final ProductRepository repository;
    private final ProductItemRepository productItemRepository;
    private final IngredientRepository ingredientRepository;

    @Autowired
    public ProductService(ProductRepository repository, ProductItemRepository productItemRepository, IngredientRepository ingredientRepository) {
        this.repository = repository;
        this.productItemRepository = productItemRepository;
        this.ingredientRepository = ingredientRepository;
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
    @Transactional
    public OwnResponse create(ProductDto dto) {
        try {
            if (repository.existsByName(dto.getName())) return OwnResponse.PRODUCT_ALREADY_EXISTS;
            Product product = dto.toEntity();

            // if product type is 'Product', it will require some props
            if (product.getType() == ProductType.PRODUCT) {
                if (dto.getIngredients() == null) return OwnResponse.INGREDIENTS_NOT_FOUND;

                // making ingredients set
                product.setIngredients(makeIngredients(dto.getIngredients()));
                Optional<ItemForCollection> first = product.getIngredients().stream().findFirst();
                if (first.isPresent()) {
                    boolean b = product.getIngredients().stream().noneMatch(
                            item -> Objects.equals(item.getProductItem().getWarehouse().getId(), first.get().getProductItem().getWarehouse().getId())
                    );
                    if (b) throw new TypesInError("Ingredients must be in one warehouse");
                }
            }

            // saving product
            repository.save(product);
            return OwnResponse.CREATED_SUCCESSFULLY;
        } catch (NotFoundException e) {
            return OwnResponse.INGREDIENTS_NOT_FOUND.setMessage(e.getMessage());
        } catch (TypesInError e) {
            return OwnResponse.INPUT_TYPE_ERROR.setMessage(e.getMessage());
        } catch (Exception e) {
            return OwnResponse.ERROR.setMessage(e.getMessage());
        }

    }

    @Override
    @Transactional
    public OwnResponse update(ProductDto dto, Long id) {
        if (repository.existsByNameAndIdIsNot(dto.getName(), id)) return OwnResponse.PRODUCT_ALREADY_EXISTS;
        return repository.findById(id).map(
                product -> {
                    product.setName(dto.getName());
                    product.setPrice(dto.getPrice());

                    // deleting old ingredients
                    ingredientRepository.deleteAll(product.getIngredients());

                    // making new ingredients set
                    product.setIngredients(makeIngredients(dto.getIngredients()));

                    repository.save(product);
                    return OwnResponse.UPDATED_SUCCESSFULLY;
                }).orElse(OwnResponse.PRODUCT_NOT_FOUND);
    }

    @Override
    public Set<ItemForCollection> getIngredientsWithProductId(Long id) {
        return repository.findById(id).map(Product::getIngredients).orElse(null);
    }

    // todo: add delete method

    private Set<ItemForCollection> makeIngredients(List<ProductItemListDto> ingredients) {
        return ingredients.stream().map(
                ingredient -> {
                    ItemForCollection ingredientProduct = new ItemForCollection();
                    ingredientProduct.setProductItem(
                            productItemRepository.findById(
                                    ingredient.getProductItemId()
                            ).orElseThrow(
                                    () -> new NotFoundException("Product item with id " + ingredient.getProductItemId() + " not found")
                            )
                    );
                    ingredientProduct.setItemAmount(ingredient.getAmount());
                    return ingredientProduct;
                }
        ).collect(Collectors.toSet());
    }

    @Override
    public List<Product> getAllByType(ProductType type) {
        return repository.findAllByType(type);
    }
}
