package khantorecrm.controller;

import khantorecrm.payload.dao.OwnResponse;
import khantorecrm.payload.dto.PaymentConfirmDto;
import khantorecrm.payload.dto.SaleDto;
import khantorecrm.service.impl.SaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Objects;

@RestController
@RequestMapping(value = "sale")
@PreAuthorize(value = "hasAnyRole('ADMIN', 'DRIVER', 'SELLER', 'LOADER', 'SUPER_LOADER')")
public class SaleController {
    private final SaleService service;

    @Autowired
    public SaleController(SaleService service) {
        this.service = service;
    }

    @GetMapping(value = "all")
    public HttpEntity<?> getAllSales() {
        return ResponseEntity.ok(service.getAllInstances());
    }

    @PostMapping(value = "sell")
    public HttpEntity<?> sellProducts(@RequestBody @Valid SaleDto dto) {
        return service.sell(dto).handleResponse();
    }

    @GetMapping(value = "payment-sums-period")
    public HttpEntity<?> getAllSalesByClientName(@Param("createdById") Long createdById,
                                                 @Param("startDate") String startDate,
                                                 @Param("endDate") String endDate) {
        return OwnResponse.ALL_DATA.setData(service.getPaymentSumsByPeriod(
                createdById,
                startDate,
                endDate
        )).handleResponse();
    }

    @DeleteMapping(value = "delete/{id}")
    public HttpEntity<?> deleteSale(@PathVariable Long id) {
        return service.delete(id).handleResponse();
    }

    @PostMapping(value = "confirm-payment")
    public HttpEntity<?> confirmPayment(@RequestBody PaymentConfirmDto confirmDto) {
        return service.confirmPayment(confirmDto).handleResponse();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public HttpEntity<?> handleValidationExceptions(MethodArgumentNotValidException ex) {
        return OwnResponse.INPUT_TYPE_ERROR.setMessage(Objects.requireNonNull(ex.getBindingResult().getFieldError()).getDefaultMessage()).handleResponse();
    }
}
