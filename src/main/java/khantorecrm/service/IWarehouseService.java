package khantorecrm.service;

import khantorecrm.model.ProductItem;
import khantorecrm.payload.dao.OwnResponse;
import khantorecrm.payload.dto.ProductList;

import java.util.List;

public interface IWarehouseService {
    List<ProductItem> getAllItemsWithWarehouseId(Long id);

    OwnResponse addItemToWarehouse(Long id, ProductList dto);
}
