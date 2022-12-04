package khantorecrm.controller;

import khantorecrm.service.impl.ProductItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "product-item")
@PreAuthorize(value = "hasAnyRole('ADMIN', 'DRIVER', 'SELLER')")
public class ProductItemController {
    private final ProductItemService service;

    @Autowired
    public ProductItemController(ProductItemService service) {
        this.service = service;
    }
}
