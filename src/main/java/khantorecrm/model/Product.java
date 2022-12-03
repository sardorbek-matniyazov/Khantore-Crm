package khantorecrm.model;

import com.fasterxml.jackson.annotation.JsonValue;
import khantorecrm.model.base.BaseEntity;
import khantorecrm.model.enums.ProductType;
import khantorecrm.utils.constants.NamingConstants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
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
//caching
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Product extends BaseEntity implements Serializable {
    @Column(name = "product_name", length = NamingConstants.MODEL_NAME_LENGTH, unique = true)
    private String name;

    @Column(name = "product_price")
    private Double price = 0.0;

    @Enumerated(EnumType.STRING)
    @Column(name = "product_type", length = NamingConstants.MODEL_ENUM_LENGTH)
    private ProductType type;

    @OneToMany(
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE},
            fetch = FetchType.LAZY
    )
    @JoinColumn(name = "product_fk", referencedColumnName = "id")
    private Set<ItemForCollection> ingredients = new HashSet<>();

    public Product(String name, Double price, ProductType type) {
        this.name = name;
        this.price = price;
        this.type = type;
    }

    @JsonValue
    public Map<String, Object> toJson() {
        Map<String, Object> result = new HashMap<>();
        result.put("productId", super.getId());

        result.put("name", this.getName());
        result.put("price", this.getPrice());
        return result;
    }
}
