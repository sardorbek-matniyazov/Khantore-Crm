package khantorecrm.model;

import com.fasterxml.jackson.annotation.JsonValue;
import khantorecrm.model.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.util.HashMap;
import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity(name = "product_item")
@NoArgsConstructor
@AllArgsConstructor
public class ProductItem extends BaseEntity {
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    @ManyToOne(optional = false)
    private Product itemProduct;

    @Column(name = "item_amount")
    private Double itemAmount = 0.0;

    @OnDelete(action = OnDeleteAction.CASCADE)
    @ManyToOne(optional = false, cascade = CascadeType.PERSIST)
    private Warehouse warehouse;

    public ProductItem(Product itemProduct, Double itemAmount) {
        this.itemProduct = itemProduct;
        this.itemAmount = itemAmount;
    }

    @JsonValue
    public Map<String, Object> toJson() {
        Map<String, Object> result = new HashMap<>();
        result.put("productItemId", super.getId());

        result.put("product", this.getItemProduct().getName());
        result.put("productId", this.getItemProduct().getId());
        result.put("productPrice", this.getItemProduct().getPrice());
        result.put("productAmount", this.getItemAmount());
        result.put("warehouseId", this.getWarehouse().getId());
        result.put("warehouseName", this.getWarehouse().getName());
        return result;
    }
}
