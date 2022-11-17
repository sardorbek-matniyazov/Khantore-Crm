package khantorecrm.controller;

import khantorecrm.service.impl.ProductItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "product-item")
public class ProductItemRepository {
    private final ProductItemService service;

    @Autowired
    public ProductItemRepository(ProductItemService service) {
        this.service = service;
    }
}
