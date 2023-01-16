package khantorecrm.controller;

import khantorecrm.payload.dao.OwnResponse;
import khantorecrm.service.impl.ProductItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "product-item")
public class ProductItemController {
    private final ProductItemService service;

    @Autowired
    public ProductItemController(ProductItemService service) {
        this.service = service;
    }

    @GetMapping(value = "warning")
    public HttpEntity<?> getAllWarningIngredients() {
        return OwnResponse.ALL_DATA.setData(service.getAllWarningIngredients()).handleResponse();
    }
}
