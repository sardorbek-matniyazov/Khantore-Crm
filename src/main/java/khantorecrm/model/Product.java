package khantorecrm.model;

import com.fasterxml.jackson.annotation.JsonValue;
import khantorecrm.model.base.BaseEntity;
import khantorecrm.model.enums.ProductType;
import khantorecrm.utils.constants.Constants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity(name = "product")
@NoArgsConstructor
@AllArgsConstructor
public class Product extends BaseEntity implements Serializable {
    @Column(name = "product_name", length = Constants.MODEL_NAME_LENGTH, unique = true)
    private String name;

    @Column(name = "product_price")
    private Double price = 0.0;

    @Column(name = "product_warning_amount")
    private Double warningAmount = 0.0;

    @Enumerated(EnumType.STRING)
    @Column(name = "product_type", length = Constants.MODEL_ENUM_LENGTH)
    private ProductType type;

    @OneToMany(
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE},
            fetch = FetchType.LAZY
    )
    @JoinColumn(name = "product_fk", referencedColumnName = "id")
    private Set<ItemForCollection> ingredients = new HashSet<>();

    public Product(String name, Double price, ProductType type, @NotNull(message = "Warning Amount not be null") Double warningAmount) {
        this.name = name;
        this.price = price;
        this.type = type;
        this.warningAmount = warningAmount;
    }

    @JsonValue
    public Map<String, Object> toJson() {
        Map<String, Object> result = new HashMap<>();
        result.put("productId", super.getId());

        result.put("name", this.getName());
        result.put("price", this.getPrice());
        result.put("warningAmount", this.getWarningAmount());
        return result;
    }
}
