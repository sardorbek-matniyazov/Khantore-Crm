package khantorecrm.repository;

import khantorecrm.model.ProductItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductItemRepository extends JpaRepository<ProductItem, Long> {

    boolean existsById(Long id);

    List<ProductItem> findAllByWarehouseId(Long id);

    boolean existsByWarehouseIdAndItemProduct_Id(Long warehouseId, Long productId);
}
