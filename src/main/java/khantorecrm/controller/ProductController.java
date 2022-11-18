package khantorecrm.controller;

import khantorecrm.payload.dao.OwnResponse;
import khantorecrm.payload.dto.ProductDto;
import khantorecrm.service.impl.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "product")
public class ProductController {
    private final ProductService service;

    @Autowired
    public ProductController(ProductService service) {
        this.service = service;
    }

    @GetMapping(value = "all")
    public HttpEntity<?> getAllProducts() {
        return OwnResponse.ALL_DATA.setData(service.getAllInstances()).handleResponse();
    }

    @GetMapping(value = "{id}")
    public HttpEntity<?> getProductWithId(@PathVariable Long id) {
        return OwnResponse.ALL_DATA.setData(service.getInstanceWithId(id)).handleResponse();
    }

    @GetMapping(value = "{id}/ingredients")
    public HttpEntity<?> getProductIngredientsWithId(@PathVariable Long id) {
        return OwnResponse.ALL_DATA.setData(service.getIngredientsWithProductId(id)).handleResponse();
    }

    @PostMapping(value = "create")
    public HttpEntity<?> createProduct(@RequestBody @Valid ProductDto dto) {
        return service.create(dto).handleResponse();
    }

    @PutMapping(value = "{id}")
    public HttpEntity<?> updateProduct(@RequestBody @Valid ProductDto dto, @PathVariable Long id) {
        return service.update(dto, id).handleResponse();
    }
}
