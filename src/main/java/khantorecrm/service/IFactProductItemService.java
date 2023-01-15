package khantorecrm.service;

import khantorecrm.payload.dao.OwnResponse;
import khantorecrm.payload.dao.projection.FactDailyProjection;
import khantorecrm.payload.dto.FactProductItemDailyDto;

import java.util.List;

public interface IFactProductItemService {
    OwnResponse createDailyFactProductItem(FactProductItemDailyDto dto);
    OwnResponse updateDailyFactProductItemWithId(FactProductItemDailyDto dto);

    List<FactDailyProjection> getFactDailyWarehouseProducts(Long warehouseId);
}
