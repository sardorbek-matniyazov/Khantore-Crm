package khantorecrm.controller;

import khantorecrm.payload.dao.OwnResponse;
import khantorecrm.payload.dto.FactProductItemDailyDto;
import khantorecrm.service.impl.FactProductItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Objects;

@RestController
@RequestMapping(value = "factProductItem")
@PreAuthorize(value = "hasAnyRole('ADMIN')")
public class FactProductItemController {
    private final FactProductItemService service;

    @Autowired
    public FactProductItemController(FactProductItemService service) {
        this.service = service;
    }

    @PostMapping(value = "createDailyFactProductItem")
    private HttpEntity<?> createDailyFactProductItem(@RequestBody @Valid FactProductItemDailyDto dto) {
        return service.createDailyFactProductItem(dto).handleResponse();
    }

    @PutMapping(value = "createDailyFactProductItem")
    private HttpEntity<?> updateDailyFactProductItem(@RequestBody @Valid FactProductItemDailyDto dto) {
        return service.updateDailyFactProductItemWithId(dto).handleResponse();
    }

    @GetMapping(value = "factDailyWarehouse/{warehouseId}")
    private HttpEntity<?> getFactDailyByWarehouse(@PathVariable Long warehouseId) {
        return OwnResponse.ALL_DATA.setData(service.getFactDailyWarehouseProducts(warehouseId)).handleResponse();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public HttpEntity<?> handleValidationExceptions(MethodArgumentNotValidException ex) {
        return OwnResponse.INPUT_TYPE_ERROR.setMessage(Objects.requireNonNull(ex.getBindingResult().getFieldError()).getDefaultMessage()).handleResponse();
    }
}