package khantorecrm.controller;

import khantorecrm.payload.dao.OwnResponse;
import khantorecrm.payload.dto.ProductList;
import khantorecrm.payload.dto.WarehouseDto;
import khantorecrm.service.impl.WarehouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "warehouse")
public class WarehouseController {
    private final WarehouseService service;

    @Autowired
    public WarehouseController(WarehouseService service) {
        this.service = service;
    }

    @GetMapping(value = "all")
    public HttpEntity<?> getAllWarehouses() {
        return OwnResponse.ALL_DATA.setData(service.getAllInstances()).handleResponse();
    }

    @GetMapping(value = "{id}")
    public HttpEntity<?> getWarehouseWithId(@PathVariable Long id) {
        return OwnResponse.ALL_DATA.setData(service.getInstanceWithId(id)).handleResponse();
    }

    @GetMapping(value = "{id}/items")
    public HttpEntity<?> getAllItemsWithWarehouseId(@PathVariable Long id) {
        return OwnResponse.ALL_DATA.setData(service.getAllItemsWithWarehouseId(id)).handleResponse();
    }

    @PostMapping(value = "create")
    public HttpEntity<?> createProduct(@RequestBody @Valid WarehouseDto dto) {
        return service.create(dto).handleResponse();
    }

    @PostMapping(value = "{id}/addItem")
    public HttpEntity<?> addItemToWarehouse(@RequestBody @Valid ProductList dto, @PathVariable Long id) {
        return service.addItemToWarehouse(id, dto).handleResponse();
    }

    @PutMapping(value = "{id}")
    public HttpEntity<?> updateWarehouse(@RequestBody @Valid WarehouseDto dto, @PathVariable Long id) {
        return service.update(dto, id).handleResponse();
    }
}
