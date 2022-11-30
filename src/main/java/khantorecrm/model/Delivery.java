package khantorecrm.model;

import com.fasterxml.jackson.annotation.JsonValue;
import khantorecrm.model.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import java.util.HashMap;
import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity(name = "delivery")
@NoArgsConstructor
@AllArgsConstructor
//caching
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Delivery extends BaseEntity {
    @OneToOne(optional = false)
    private User deliverer;

    @OneToOne(optional = false)
    private Warehouse baggage;

    @JsonValue
    public Map<String, Object> toJson() {
        Map<String, Object> result = new HashMap<>();
        result.put("deliveryId", super.getId());
        result.put("createdAt", super.getCreatedAt());

        result.put("name", this.deliverer.getName());
        result.put("baggage", this.baggage);

        // todo: role settings should be here
        return result;
    }
}