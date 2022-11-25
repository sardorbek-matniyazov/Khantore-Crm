package khantorecrm.service;

import khantorecrm.payload.dao.OwnResponse;
import khantorecrm.payload.dto.ReturnProductDto;

public interface IDeliveryService {
    OwnResponse getBaggageWithDeliveryId(Long id);

    OwnResponse returnSelectedProduct(ReturnProductDto dto);

    OwnResponse acceptReturnedProduct(Long inputId);

    OwnResponse rejectReturnedProduct(Long inputId);
}
