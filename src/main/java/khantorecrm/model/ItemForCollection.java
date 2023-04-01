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
@Entity(name = "items_for_collection")
@NoArgsConstructor
@AllArgsConstructor
public class ItemForCollection extends BaseEntity {
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    @ManyToOne(optional = false, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private ProductItem productItem;

    @Column(name = "item_for_collection_amount")
    private Double itemAmount = 0.0;

    @Column(name = "item_for_collection_cr_pr_price")
    private Double currentProductPrice = 0.0;

    @Column(name = "item_for_collection_how_much_ingredient")
    private Double howMuchIngredient = 0.0;

    @Column(name = "item_for_collection_for_how_much_product")
    private Double forHowMuchProduct = 0.0;

    @Column(name = "item_for_collection_cr_ingr_price")
    private Double currentIngredientPrice = 0.0;

    public ItemForCollection(ProductItem baggageItem, Double amount, Double price, double sum) {
        this.productItem = baggageItem;
        this.itemAmount = amount;
        this.currentProductPrice = price;
        this.currentIngredientPrice = sum;
    }

    @JsonValue
    public Map<String, Object> toJson() {
        Map<String, Object> result = new HashMap<>();
        result.put("ingredientId", super.getId());

        result.put("itemAmount", this.getItemAmount());
        result.put("forHowMuchProduct", this.getForHowMuchProduct());
        result.put("howMuchIngredient", this.getHowMuchIngredient());
        result.put("productItem", this.getProductItem());
        result.put("currentProductPrice", this.getCurrentProductPrice());
        return result;
    }
}
