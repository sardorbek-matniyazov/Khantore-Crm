package khantorecrm.model;

import com.fasterxml.jackson.annotation.JsonValue;
import khantorecrm.model.base.BaseEntity;
import khantorecrm.model.enums.OutputType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity(name = "output")
@NoArgsConstructor
@AllArgsConstructor
//caching
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Output extends BaseEntity {
    @OneToMany(orphanRemoval = true,
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE},
            fetch = FetchType.LAZY
    )
    @JoinColumn(name = "output_fk", referencedColumnName = "id")
    private Set<ItemForCollection> productItems;

    @Enumerated(EnumType.STRING)
    @Column(name = "output_type", length = 10, nullable = false)
    private OutputType type;

    @ManyToOne
    private Delivery delivery;

    public Output(
            Set<ItemForCollection> productItems,
            OutputType sale
    ) {
        this.productItems = productItems;
        this.type = sale;
    }

    @JsonValue
    public Map<String, Object> toJson() {
        Map<String, Object> result = new HashMap<>();
        result.put("outputId", super.getId());
        result.put("createdAt", super.getCreatedAt());
        result.put("createdBy", this.getCreatedBy().getName());

        result.put("outputType", this.type);
        result.put("productItems", this.productItems);
        return result;
    }
}
