package khantorecrm.service;

import khantorecrm.model.FactProductItemDaily;
import khantorecrm.payload.dao.OwnResponse;
import khantorecrm.payload.dto.FactProductItemDailyDto;

import java.util.List;

/**
 * @author :  Sardor Matniyazov
 * @mailto :  sardorbekmatniyazov03@gmail.com
 * @created : 28 Mar 2023
 **/
public interface IFactService {
    OwnResponse createOrUpdateDailyFactProductItem(FactProductItemDailyDto dto);

    List<FactProductItemDaily> getFactDailyWarehouseProducts(Long warehouseId);
}
