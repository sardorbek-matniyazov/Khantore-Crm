package khantorecrm.service.impl;

import khantorecrm.model.Product;
import khantorecrm.model.enums.ProductType;
import khantorecrm.repository.ProductRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ProductServiceTest {

    @Autowired
    @Mock
    private ProductRepository repository;

    @BeforeEach
    void setUp() {
//        repository.deleteAll();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getAllInstances() {
        Product product = new Product("Test-Banana", 12.0, ProductType.INGREDIENT, 12.0);
        repository.save(product);

        List<Product> all = repository.findAll();
        Assertions.assertThat(all).extracting(Product::getPrice).containsOnly(12.0);

        repository.deleteAll();
        Assertions.assertThat(repository.findAll()).isEmpty();
    }

    @Test
    void getInstanceWithId() {
    }

    @Test
    void create() {
    }

    @Test
    void update() {
    }
}