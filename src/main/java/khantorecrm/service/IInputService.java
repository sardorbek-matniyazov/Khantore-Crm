package khantorecrm.service;

import khantorecrm.model.Input;
import khantorecrm.model.enums.ActionType;
import khantorecrm.model.enums.ProductType;
import khantorecrm.payload.dao.OwnResponse;
import khantorecrm.payload.dto.InputDto;
import khantorecrm.payload.dto.ProductItemList;
import khantorecrm.service.functionality.FindableByProductType;

import java.util.List;

public interface IInputService extends FindableByProductType<Input> {
    OwnResponse incomeIngredient(InputDto dto);

    OwnResponse incomeProduct(ProductItemList dto);

    OwnResponse production(List<ProductItemList> dto);

    List<Input> getAllInputsByStatus(ActionType wait);
}
