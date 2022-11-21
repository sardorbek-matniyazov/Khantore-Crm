package khantorecrm.repository;

import khantorecrm.model.Warehouse;
import khantorecrm.model.enums.ProductType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {
    boolean existsByName(String name);

    boolean existsById(Long id);

    List<Warehouse> findAllByType(ProductType type);
}
