package khantorecrm.controller;

import khantorecrm.payload.dao.OwnResponse;
import khantorecrm.payload.dto.FactProductItemDailyDto;
import khantorecrm.service.impl.ProductFactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Objects;

@RestController
@RequestMapping(value = "fact-product")
@PreAuthorize(value = "hasAnyRole('ADMIN', 'DRIVER', 'SELLER', 'LOADER', 'SUPER_LOADER')")
public class ProductFactController {
    private final ProductFactService service;

    @Autowired
    public ProductFactController(ProductFactService service) {
        this.service = service;
    }

    @RequestMapping(value = "daily-fact", method = {RequestMethod.POST, RequestMethod.PUT})
    private HttpEntity<?> dailyFactProductItem(@RequestBody @Valid FactProductItemDailyDto dto) {
        return service.createOrUpdateDailyFactProductItem(dto).handleResponse();
    }

    @GetMapping(value = "daily-fact-warehouse")
    private HttpEntity<?> getFactDailyByWarehouse() {
        return OwnResponse.ALL_DATA.setData(service.getFactDailyWarehouseProducts()).handleResponse();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public HttpEntity<?> handleValidationExceptions(MethodArgumentNotValidException ex) {
        return OwnResponse.INPUT_TYPE_ERROR.setMessage(Objects.requireNonNull(ex.getBindingResult().getFieldError()).getDefaultMessage()).handleResponse();
    }

}
