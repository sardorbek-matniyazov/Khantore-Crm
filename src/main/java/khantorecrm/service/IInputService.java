package khantorecrm.service;

import khantorecrm.payload.dao.OwnResponse;
import khantorecrm.payload.dto.InputDto;
import khantorecrm.payload.dto.ProductItemList;

import java.util.List;

public interface IInputService {
    OwnResponse incomeIngredient(InputDto dto);

    OwnResponse incomeProduct(ProductItemList dto);

    OwnResponse production(List<ProductItemList> dto);
}
