package khantorecrm.controller;

import khantorecrm.payload.dao.OwnResponse;
import khantorecrm.payload.dto.FactProductItemDailyDto;
import khantorecrm.service.impl.FactProductItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Objects;

@RestController
@RequestMapping(value = "fact-product-item")
@PreAuthorize(value = "hasAnyRole('ADMIN')")
public class FactProductItemController {
    private final FactProductItemService service;

    @Autowired
    public FactProductItemController(FactProductItemService service) {
        this.service = service;
    }

    @PostMapping(value = "daily-fact-product-item")
    private HttpEntity<?> createDailyFactProductItem(@RequestBody @Valid FactProductItemDailyDto dto) {
        return service.createDailyFactProductItem(dto).handleResponse();
    }

    @PutMapping(value = "daily-fact-product-item")
    private HttpEntity<?> updateDailyFactProductItem(@RequestBody @Valid FactProductItemDailyDto dto) {
        return service.updateDailyFactProductItemWithId(dto).handleResponse();
    }

    @GetMapping(value = "fact-daily-warehouse/{warehouseId}")
    private HttpEntity<?> getFactDailyByWarehouse(@PathVariable Long warehouseId) {
        return OwnResponse.ALL_DATA.setData(service.getFactDailyWarehouseProducts(warehouseId)).handleResponse();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public HttpEntity<?> handleValidationExceptions(MethodArgumentNotValidException ex) {
        return OwnResponse.INPUT_TYPE_ERROR.setMessage(Objects.requireNonNull(ex.getBindingResult().getFieldError()).getDefaultMessage()).handleResponse();
    }
}