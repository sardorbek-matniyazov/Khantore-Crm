package khantorecrm.service;

import khantorecrm.payload.dao.OwnResponse;
import khantorecrm.payload.dto.InputDto;

public interface IInputService {
    OwnResponse incomeIngredient(InputDto dto);
}
