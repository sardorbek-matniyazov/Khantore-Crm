package khantorecrm.repository;

import khantorecrm.model.ProductItem;
import khantorecrm.model.enums.ProductType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductItemRepository extends JpaRepository<ProductItem, Long> {
    List<ProductItem> findAllByWarehouseId(Long id);

    boolean existsByWarehouseIdAndItemProduct_Id(Long warehouseId, Long productId);

    List<ProductItem> findAllByItemProduct_TypeAndWarehouse_Type(ProductType p_type, ProductType w_type);
}
