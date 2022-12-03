package khantorecrm.service;

import khantorecrm.model.ProductItem;
import khantorecrm.model.Warehouse;
import khantorecrm.payload.dao.OwnResponse;
import khantorecrm.payload.dto.MovingItemDto;
import khantorecrm.payload.dto.ProductList;
import khantorecrm.service.functionality.FindableByProductType;

import java.util.List;

public interface IWarehouseService extends FindableByProductType<Warehouse> {
    List<ProductItem> getAllItemsWithWarehouseId(Long id);

    OwnResponse addItemToWarehouse(Long id, ProductList dto);

    OwnResponse moveItem(MovingItemDto dto);
}
