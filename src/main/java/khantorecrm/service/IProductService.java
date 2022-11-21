package khantorecrm.service;

import khantorecrm.model.Product;

import java.util.List;

public interface IProductService {
    Object getIngredientsWithProductId(Long id);

    List<Product> getAllProducts();

    List<Product> getAllIngredientProducts();
}
