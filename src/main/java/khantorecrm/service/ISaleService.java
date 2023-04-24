package khantorecrm.service;

import khantorecrm.model.Payment;
import khantorecrm.model.Sale;
import khantorecrm.payload.dao.OwnResponse;
import khantorecrm.payload.dto.PaymentConfirmDto;
import khantorecrm.payload.dto.SaleDto;

import java.util.List;

public interface ISaleService {
    OwnResponse sell(SaleDto dto);

    List<Sale> getAllInstancesByClientName(Long clientId);

    OwnResponse confirmPayment(PaymentConfirmDto confirmDto);

    List<Payment> getPaymentSumsByPeriod(Long createdById, String startDate, String endDate);
}
