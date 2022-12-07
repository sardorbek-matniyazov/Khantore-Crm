package khantorecrm.controller;

import khantorecrm.payload.dao.OwnResponse;
import khantorecrm.payload.dto.DeliveryDto;
import khantorecrm.payload.dto.ReturnProductDto;
import khantorecrm.service.impl.DeliveryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "delivery")
@PreAuthorize(value = "hasAnyRole('ADMIN', 'DRIVER', 'SELLER')")
public class DeliveryController {
    private final DeliveryService service;

    @Autowired
    public DeliveryController(DeliveryService service) {
        this.service = service;
    }

    @GetMapping(value = "all")
    public HttpEntity<?> getAllDeliveries() {
        return OwnResponse.ALL_DATA.setData(service.getAllInstances()).handleResponse();
    }

    @GetMapping(value = "{id}")
    public HttpEntity<?> getDeliveryWithId(@PathVariable Long id) {
        return OwnResponse.ALL_DATA.setData(service.getInstanceWithId(id)).handleResponse();
    }

    @GetMapping(value = "orders")
    public HttpEntity<?> getAllOrders() {
        return OwnResponse.ALL_DATA.setData(service.getAllOrders()).handleResponse();
    }

    @GetMapping(value = "{id}/wait-returns")
    public HttpEntity<?> getAllWaitReturnsWithDelivererId(@PathVariable Long id) {
        return OwnResponse.ALL_DATA.setData(service.getAllWaitReturnsWithId(id)).handleResponse();
    }

    @GetMapping(value = "{id}/orders")
    public HttpEntity<?> getAllOrdersWithDriverId(@PathVariable Long id) {
        return OwnResponse.ALL_DATA.setData(service.getAllOrdersByDriverId(id)).handleResponse();
    }

    @GetMapping(value = "wait-returns")
    public HttpEntity<?> getAllWaitReturns() {
        return OwnResponse.ALL_DATA.setData(service.getAllWaitReturns()).handleResponse();
    }

    @PostMapping(value = "order")
    public HttpEntity<?> createDelivery(@RequestBody DeliveryDto dto) {
        return service.create(dto).handleResponse();
    }

    @GetMapping(value = "{id}/baggage")
    public HttpEntity<?> getBaggageItemsWithDeliveryId(@PathVariable Long id) {
        return service.getBaggageWithDeliveryId(id).handleResponse();
    }

    @PostMapping(value = "return-product")
    public HttpEntity<?> returnSelectedProduct(@RequestBody @Valid ReturnProductDto dto) {
        return service.returnSelectedProduct(dto).handleResponse();
    }

    @PostMapping(value = "accept/{inputId}")
    public HttpEntity<?> acceptReturnedProduct(@PathVariable Long inputId) {
        return service.acceptReturnedProduct(inputId).handleResponse();
    }

    @PostMapping(value = "reject/{inputId}")
    public HttpEntity<?> rejectReturnedProduct(@PathVariable Long inputId) {
        return service.rejectReturnedProduct(inputId).handleResponse();
    }
}
