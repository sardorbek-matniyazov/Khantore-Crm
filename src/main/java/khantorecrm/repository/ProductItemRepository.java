package khantorecrm.repository;

import khantorecrm.model.ProductItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductItemRepository extends JpaRepository<ProductItem, Long> {

    @Query("select case when count(c)> 0 then true else false end from khantorecrm.model.ProductItem c where c.id = ?1")
    boolean existsById(Long id);

    List<ProductItem> findAllByWarehouseId(Long id);

    boolean existsByWarehouseId(Long id);
}
