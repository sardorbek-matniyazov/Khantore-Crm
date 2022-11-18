package khantorecrm.controller;

import khantorecrm.payload.dao.OwnResponse;
import khantorecrm.payload.dto.InputDto;
import khantorecrm.service.impl.InputService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "input")
public class InputController {
    private final InputService service;

    @Autowired
    public InputController(InputService service) {
        this.service = service;
    }

    @GetMapping(value = "all")
    public HttpEntity<?> getAllInputs() {
        return OwnResponse.ALL_DATA.setData(service.getAllInstances()).handleResponse();
    }

    @GetMapping(value = "{id}")
    public HttpEntity<?> getInputWithId(@PathVariable Long id) {
        return OwnResponse.ALL_DATA.setData(service.getInstanceWithId(id)).handleResponse();
    }

    @PostMapping(value = "income-ingredient")
    public HttpEntity<?> incomeIngredients(@RequestBody @Valid InputDto dto) {
        return service.incomeIngredient(dto).handleResponse();
    }
}
