package khantorecrm.repository;

import khantorecrm.model.ProductItem;
import khantorecrm.model.enums.ProductType;
import khantorecrm.payload.dao.projection.ProductItemDeliveryProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductItemRepository extends JpaRepository<ProductItem, Long> {
    List<ProductItem> findAllByWarehouseId(Long id);

    @Query(
            value = "with bum as (select pi.id as productItemId, * from product_item pi join product_prices_for_sellers ppfs on pi.item_product_id=ppfs.product_id) " +
                    "select p.product_name as product, piw.price as productPrice, piw.item_amount as productAmount, piw.warehouse_id as warehouseId, piw.wr_name as warehouseName, piw.productItemId " +
                    "from (bum join warehouse w on bum.warehouse_id=w.id) piw join product p on piw.product_id=p.id where piw.warehouse_id = ?1 and piw.deliverer_id = ?2",
            nativeQuery = true
    )
    List<ProductItemDeliveryProjection> findAllBaggageItemByDeliveryWarehouseId(Long warehouseId, Long deliveryId);

    boolean existsByWarehouseIdAndItemProduct_Id(Long warehouseId, Long productId);

    List<ProductItem> findAllByItemProduct_TypeAndWarehouse_Type(ProductType p_type, ProductType w_type);
}
