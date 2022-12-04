package khantorecrm.controller;

import khantorecrm.model.enums.ProductType;
import khantorecrm.payload.dao.OwnResponse;
import khantorecrm.payload.dto.MovingItemDto;
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

    @GetMapping(value = "ingredients")
    public HttpEntity<?> getAllIngredientWarehouses() {
        return OwnResponse.ALL_DATA.setData(service.getAllByType(ProductType.INGREDIENT)).handleResponse();
    }

    @GetMapping(value = "products")
    public HttpEntity<?> getAllProductWarehouses() {
        return OwnResponse.ALL_DATA.setData(service.getAllByType(ProductType.PRODUCT)).handleResponse();
    }

    @GetMapping(value = "ingredient/items")
    public HttpEntity<?> getAllIngredientItemsWarehouses() {
        return OwnResponse.ALL_DATA.setData(service.getAllItemsByType(ProductType.INGREDIENT)).handleResponse();
    }

    @GetMapping(value = "products/items")
    public HttpEntity<?> getAllProductItemsWarehouses() {
        return OwnResponse.ALL_DATA.setData(service.getAllItemsByType(ProductType.PRODUCT)).handleResponse();
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
    public HttpEntity<?> createWarehouse(@RequestBody @Valid WarehouseDto dto) {
        return service.create(dto).handleResponse();
    }

    @PostMapping(value = "{id}/addItem")
    public HttpEntity<?> addItemToWarehouse(@RequestBody @Valid ProductList dto, @PathVariable Long id) {
        return service.addItemToWarehouse(id, dto).handleResponse();
    }

    @PostMapping(value = "move-item")
    public HttpEntity<?> movingItemsOfWarehouse(@RequestBody @Valid MovingItemDto dto) {
        return service.moveItem(dto).handleResponse();
    }

    @PutMapping(value = "{id}")
    public HttpEntity<?> updateWarehouse(@RequestBody @Valid WarehouseDto dto, @PathVariable Long id) {
        return service.update(dto, id).handleResponse();
    }
}
