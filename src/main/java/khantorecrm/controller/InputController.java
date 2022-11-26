package khantorecrm.controller;

import khantorecrm.model.enums.ActionType;
import khantorecrm.model.enums.ProductType;
import khantorecrm.payload.dao.OwnResponse;
import khantorecrm.payload.dto.InputDto;
import khantorecrm.payload.dto.ProductItemList;
import khantorecrm.service.impl.InputService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "input")
public class InputController {
    private final InputService service;

    @Autowired
    public InputController(InputService service) {
        this.service = service;
    }

    @GetMapping(value = "ingredients")
    public HttpEntity<?> getAllIngredients() {
        return OwnResponse.ALL_DATA.setData(service.getAllByType(ProductType.INGREDIENT)).handleResponse();
    }

    @GetMapping(value = "products")
    public HttpEntity<?> getAllProducts() {
        return OwnResponse.ALL_DATA.setData(service.getAllByType(ProductType.PRODUCT)).handleResponse();
    }

    @PostMapping(value = "income-ingredient")
    public HttpEntity<?> incomeIngredients(@RequestBody @Valid InputDto dto) {
        return service.incomeIngredient(dto).handleResponse();
    }

    @PostMapping(value = "income-product")
    public HttpEntity<?> incomeProducts(@RequestBody ProductItemList dto) {
        return service.incomeProduct(dto).handleResponse();
    }

    @PostMapping(value = "production")
    public HttpEntity<?> production(@RequestBody @Valid List<ProductItemList> dto) {
        return service.production(dto).handleResponse();
    }

    @GetMapping(value = "all-wait")
    public HttpEntity<?> getAllWaitingInputs() {
        return ResponseEntity.ok(
                OwnResponse.ALL_DATA.setData(
                        service.getAllInputsByStatus(ActionType.WAIT)
                )
        );
    }
}
