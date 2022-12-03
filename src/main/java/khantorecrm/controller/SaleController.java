package khantorecrm.controller;

import khantorecrm.payload.dao.OwnResponse;
import khantorecrm.payload.dto.SaleDto;
import khantorecrm.service.impl.SaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Objects;

@RestController
@RequestMapping(value = "sale")
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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public HttpEntity<?> handleValidationExceptions(MethodArgumentNotValidException ex) {
        return OwnResponse.INPUT_TYPE_ERROR.setMessage(Objects.requireNonNull(ex.getBindingResult().getFieldError()).getDefaultMessage()).handleResponse();
    }
}
