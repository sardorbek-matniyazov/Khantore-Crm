package khantorecrm.service.impl;

import khantorecrm.repository.ProductItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductItemService {
    private final ProductItemRepository repository;

    @Autowired
    public ProductItemService(ProductItemRepository repository) {
        this.repository = repository;
    }
}
