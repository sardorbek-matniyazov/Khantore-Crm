package khantorecrm.model;

import com.fasterxml.jackson.annotation.JsonValue;
import khantorecrm.model.base.BaseEntity;
import khantorecrm.model.enums.ActionType;
import khantorecrm.model.enums.ProductType;
import khantorecrm.utils.constants.NamingConstants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import java.util.HashMap;
import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity(name = "input")
//caching
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@NoArgsConstructor
@AllArgsConstructor
public class Input extends BaseEntity {
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    @ManyToOne(optional = false, cascade = CascadeType.MERGE)
    private ProductItem productItem;

    @Column(name = "input_amount")
    private Double amount = 0.0;

    @Enumerated(EnumType.STRING)
    @Column(name = "input_product_type", length = NamingConstants.MODEL_ENUM_LENGTH)
    private ProductType type;

    @Column(name = "input_cr_pr_price")
    private Double currentProductPrice = 0.0;

    @ManyToOne(cascade = CascadeType.MERGE)
    private Employee employee;

    @Column(name = "input_status")
    private ActionType status = ActionType.WAIT;

    public Input(
            ProductItem productItem,
            Double amount,
            ProductType type,
            Double currentProductPrice,
            ActionType actionType
    ) {
        this.productItem = productItem;
        this.amount = amount;
        this.type = type;
        this.status = actionType;
        this.currentProductPrice = currentProductPrice;
    }

    @JsonValue
    public Map<String, Object> toJson() {
        Map<String, Object> result = new HashMap<>();
        result.put("inputId", super.getId());
        result.put("createdAt", super.getCreatedAt());

        result.put("amount", this.getAmount());
        result.put("product", this.getProductItem().getItemProduct().getName());
        result.put("warehouse", this.getProductItem().getWarehouse().getName());
        result.put("productPrice", this.getCurrentProductPrice());

        if (this.getEmployee() != null) {
            result.put("employee", this.getEmployee().getName());
        }
        return result;
    }
}
