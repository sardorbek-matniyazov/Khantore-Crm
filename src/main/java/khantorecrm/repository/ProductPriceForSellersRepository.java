package khantorecrm.repository;

import khantorecrm.model.ProductPricesForSellers;
import khantorecrm.payload.dao.projection.ProductPriceForSellerProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

/**
 * @author :  Sardor Matniyazov
 * @mailto :  sardorbekmatniyazov03@gmail.com
 * @created : 08 Feb 2023
 **/
public interface ProductPriceForSellersRepository extends JpaRepository<ProductPricesForSellers, String> {
    Optional<ProductPricesForSellers> findByDelivererIdAndProductId(Long delivererId, Long productId);

    @Query(
            value = "select pppfs.product_name as productName, pppfs.product_id as productId, pppfs.price as price, d.user_name as delivererName, d.id as delivererId " +
                    "from (product_prices_for_sellers ppfs join product p on p.id = ppfs.product_id) pppfs join users d on pppfs.deliverer_id=d.id " +
                    " where d.id=?1",
            nativeQuery = true
    )
    List<ProductPriceForSellerProjection> getAllByDelivererId(Long id);

}
