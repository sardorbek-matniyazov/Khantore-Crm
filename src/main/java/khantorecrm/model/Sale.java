package khantorecrm.model;

import com.fasterxml.jackson.annotation.JsonValue;
import khantorecrm.model.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import java.util.HashMap;
import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity(name = "sale")
@NoArgsConstructor
@AllArgsConstructor
//caching
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Sale extends BaseEntity {
    @OneToOne(optional = false, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    private Output output;

    @ManyToOne(optional = false)
    private Client client;

    @Column(name = "sale_whole_price", nullable = false)
    private Double price;

    @Column(name = "sale_debt_price", nullable = false)
    private Double debtPrice;

    @Column(name = "sale_paid_price", nullable = false)
    private Double paidPrice;

    @Column(name = "sale_cr_product_price", nullable = false)
    private Double productPrice;

    @JsonValue
    public Map<String, Object> toJson() {
        Map<String, Object> result = new HashMap<>();
        result.put("saleId", super.getId());
        result.put("createdAt", super.getCreatedAt());

        result.put("product", this.getOutput().getProductItem().getItemProduct().getName());
        result.put("amount", this.output.getAmount());
        result.put("client", this.client.getName());
        result.put("wholePrice", this.price);
        result.put("debtPrice", this.debtPrice);
        result.put("paidPrice", this.paidPrice);
        return result;
    }
}
