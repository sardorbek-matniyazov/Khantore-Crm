package khantorecrm.repository;

import khantorecrm.model.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {
    boolean existsByName(String name);

    boolean existsById(Long id);
}
