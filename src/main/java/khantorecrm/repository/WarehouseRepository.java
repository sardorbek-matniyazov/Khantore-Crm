package khantorecrm.repository;

import khantorecrm.model.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {
    @Query("select case when count(c)> 0 then true else false end from khantorecrm.model.Warehouse c where c.name = ?1")
    boolean existsByName(String name);

    @Query("select case when count(c)> 0 then true else false end from khantorecrm.model.Warehouse c where c.id = ?1")
    boolean existsById(Long id);
}
