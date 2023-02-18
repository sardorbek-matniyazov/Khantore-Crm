package khantorecrm.controller;

import khantorecrm.payload.dao.OwnResponse;
import khantorecrm.payload.dto.SaleDto;
import khantorecrm.service.impl.SaleService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping(value = "all/{clientId}")
    public HttpEntity<?> getAllSalesByClientName(@PathVariable Long clientId) {
        return OwnResponse.ALL_DATA.setData(service.getAllInstancesByClientName(clientId)).handleResponse();
    }

    @PostMapping(value = "sell")
    public HttpEntity<?> sellProducts(@RequestBody @Valid SaleDto dto) {
        return service.sell(dto).handleResponse();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public HttpEntity<?> handleValidationExceptions(MethodArgumentNotValidException ex) {
        return OwnResponse.INPUT_TYPE_ERROR.setMessage(Objects.requireNonNull(ex.getBindingResult().getFieldError()).getDefaultMessage()).handleResponse();
    }
}
