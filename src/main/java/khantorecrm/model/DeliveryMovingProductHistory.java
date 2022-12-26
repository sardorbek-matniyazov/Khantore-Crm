package khantorecrm.model;

import com.fasterxml.jackson.annotation.JsonValue;
import khantorecrm.model.base.BaseWithCreatedBy;
import khantorecrm.model.enums.ActionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;

import java.util.HashMap;
import java.util.Map;

import static khantorecrm.model.enums.ActionType.WAIT;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "moving_delivery_product")
public class DeliveryMovingProductHistory extends BaseWithCreatedBy {

    @ManyToOne(
            optional = false,
            cascade = {CascadeType.MERGE},
            targetEntity = Delivery.class
    )
    private Delivery fromDelivery;

    @ManyToOne(
            optional = false,
            cascade = {CascadeType.MERGE},
            targetEntity = Delivery.class
    )
    private Delivery toDelivery;

    @ManyToOne(
            optional = false,
            cascade = {CascadeType.MERGE},
            targetEntity = Product.class
    )
    private Product product;

    @Column(name = "mv_dl_pr_amount", nullable = false)
    private Double amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "mv_dl_pr_action", nullable = false)
    private ActionType action = WAIT;

    @JsonValue
    public Map<String, Object> toJson() {
        Map<String, Object> result = new HashMap<>();
        result.put("id", super.getId());
        result.put("fromDeliverer", this.getFromDelivery().getDeliverer().getName());
        result.put("toDeliverer", this.getToDelivery().getDeliverer().getName());
        result.put("product", this.getProduct().getName());
        result.put("amount", this.getAmount());
        result.put("action", this.getAction());
        return result;
    }

}
