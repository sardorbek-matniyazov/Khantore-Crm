package khantorecrm.controller;

import khantorecrm.payload.dto.SaleDto;
import khantorecrm.service.impl.SaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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

    @PostMapping(value = "create")
    public HttpEntity<?> create(@RequestBody @Valid SaleDto dto) {
        return service.create(dto).handleResponse();
    }
}
