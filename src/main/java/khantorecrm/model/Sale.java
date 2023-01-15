package khantorecrm.model;

import com.fasterxml.jackson.annotation.JsonValue;
import khantorecrm.model.base.BaseWithCreatedBy;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity(name = "sale")
@NoArgsConstructor
@AllArgsConstructor
public class Sale extends BaseWithCreatedBy {
    @OneToOne(optional = false, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    private Output output;

    @ManyToOne(optional = false, cascade = CascadeType.MERGE)
    private Client client;

    @Column(name = "sale_whole_price", nullable = false)
    private Double wholePrice;

    @Column(name = "sale_debt_price", nullable = false)
    private Double debtPrice;

    @OneToOne(
            cascade = {CascadeType.PERSIST, CascadeType.MERGE},
            orphanRemoval = true
    )
    @JoinColumn(name = "sale_fk", referencedColumnName = "id")
    private Payment payment;

    @JsonValue
    public Map<String, Object> toJson() {
        Map<String, Object> result = new HashMap<>();
        result.put("saleId", super.getId());
        result.put("createdAt", super.getCreatedAt());
        result.put("createdBy", super.getCreatedBy().getName());

        result.put("products", this.getOutput().getProductItems());
        result.put("client", this.client.getName());
        result.put("debtPrice", this.debtPrice);
        result.put("payment", this.payment);
        result.put("wholePrice", this.wholePrice);
        result.put("paidPrice", this.wholePrice - this.debtPrice);
        return result;
    }
}
