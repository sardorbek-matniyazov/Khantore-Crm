package khantorecrm.controller;

import khantorecrm.payload.dao.OwnResponse;
import khantorecrm.payload.dto.DeliveryDto;
import khantorecrm.payload.dto.ReturnProductDto;
import khantorecrm.service.impl.DeliveryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "delivery")
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
