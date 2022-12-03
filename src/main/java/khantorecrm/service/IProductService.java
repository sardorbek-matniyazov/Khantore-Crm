package khantorecrm.service;

import khantorecrm.model.ItemForCollection;
import khantorecrm.model.Product;
import khantorecrm.service.functionality.FindableByProductType;

import java.util.Set;

public interface IProductService extends FindableByProductType<Product> {
    Set<ItemForCollection> getIngredientsWithProductId(Long id);
}
