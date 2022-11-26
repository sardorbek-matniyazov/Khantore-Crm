package khantorecrm.service;

import khantorecrm.model.Ingredient;
import khantorecrm.model.Product;
import khantorecrm.service.functionality.FindableByProductType;

import java.util.List;
import java.util.Set;

public interface IProductService extends FindableByProductType<Product> {
    Set<Ingredient> getIngredientsWithProductId(Long id);
}
