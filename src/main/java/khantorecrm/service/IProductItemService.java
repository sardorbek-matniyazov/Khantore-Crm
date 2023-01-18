package khantorecrm.service;

import khantorecrm.model.FactProductItemDaily;
import khantorecrm.model.ProductItem;
import khantorecrm.payload.dao.OwnResponse;
import khantorecrm.payload.dto.FactProductItemDailyDto;

import java.util.List;

public interface IProductItemService {
    List<ProductItem> getAllWarningIngredients();
}
