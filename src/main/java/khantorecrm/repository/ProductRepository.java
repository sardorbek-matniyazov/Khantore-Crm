package khantorecrm.repository;

import khantorecrm.model.Product;
import khantorecrm.model.enums.ProductType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    boolean existsByName(String name);

    boolean existsByNameAndIdIsNot(String name, Long id);

    List<Product> findAllByType(ProductType ingredient);
}
