package khantorecrm.service;

import khantorecrm.model.FactProductItemDaily;
import khantorecrm.payload.dao.OwnResponse;
import khantorecrm.payload.dto.FactProductItemDailyDto;

import java.util.List;

public interface IFactProductItemService {
    OwnResponse createOrUpdateDailyFactProductItem(FactProductItemDailyDto dto);

    List<FactProductItemDaily> getFactDailyWarehouseProducts();
}
