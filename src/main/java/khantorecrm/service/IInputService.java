package khantorecrm.service;

import khantorecrm.model.Input;
import khantorecrm.model.enums.ActionType;
import khantorecrm.payload.dao.OwnResponse;
import khantorecrm.payload.dto.InputDto;
import khantorecrm.payload.dto.ProductItemWrapper;
import khantorecrm.service.functionality.FindableByProductType;

import java.util.List;

public interface IInputService extends FindableByProductType<Input> {
    OwnResponse incomeIngredient(InputDto dto);

    OwnResponse production(ProductItemWrapper dto);

    List<Input> getAllInputsByStatus(ActionType wait);
}
