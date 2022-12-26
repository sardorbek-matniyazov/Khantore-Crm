package khantorecrm.service;

import khantorecrm.model.Input;
import khantorecrm.model.Output;
import khantorecrm.payload.dao.OwnResponse;
import khantorecrm.payload.dto.DeliveryShareDto;
import khantorecrm.payload.dto.ReturnProductDto;

import java.util.List;

public interface IDeliveryService {
    OwnResponse getBaggageWithUserId(Long id);

    OwnResponse returnSelectedProduct(ReturnProductDto dto);

    OwnResponse acceptReturnedProduct(Long inputId);

    OwnResponse rejectReturnedProduct(Long inputId);

    List<Output> getAllOrders();

    List<Input> getAllWaitReturnsWithId(Long id);

    List<Output> getAllOrdersByDriverId(Long id);

    List<Input> getAllWaitReturns();

    OwnResponse shareWithDriver(DeliveryShareDto dto);

    OwnResponse acceptMovingProductWithDeliverer(Long movingId);

    OwnResponse rejectMovingProductWithDeliverer(Long movingId);

    OwnResponse getAllMovingWithDelivererId(Long id);
}
