package khantorecrm.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * @author :  Sardor Matniyazov
 * @mailto :  sardorbekmatniyazov03@gmail.com
 * @created : 08 Feb 2023
 **/
@NoArgsConstructor
@Data
@Entity
@Table(
        name = "product_prices_for_sellers",
        uniqueConstraints = @UniqueConstraint(columnNames = {"deliverer_id", "product_id"})
)
public class ProductPricesForSellers {
    @Column(name = "deliverer_id")
    private Long delivererId;
    @Column(name = "product_id")
    private Long productId;
    private Double price;

    @Id
    private String id = String.valueOf(delivererId).concat("_").concat(String.valueOf(productId));

    public ProductPricesForSellers(Long delivererId, Long productId, Double amount) {
        this.delivererId = delivererId;
        this.productId = productId;
        this.price = amount;
        this.id = String.valueOf(delivererId).concat("_").concat(String.valueOf(productId));
    }

    public ProductPricesForSellers setPrice(Double amount) {
        this.price = amount;
        return this;
    }
}
