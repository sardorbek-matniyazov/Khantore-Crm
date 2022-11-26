package khantorecrm.service.functionality;

import khantorecrm.model.enums.ProductType;

import java.util.List;

public interface FindableByProductType<T> {
    List<T> getAllByType(ProductType type);
}
