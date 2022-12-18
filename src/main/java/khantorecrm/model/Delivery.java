package khantorecrm.model;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import java.util.HashMap;
import java.util.Map;

@Data
@Entity(name = "delivery")
@NoArgsConstructor
@AllArgsConstructor
//caching
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Delivery {
    @Id
    private Long id;

    @OnDelete(action = OnDeleteAction.CASCADE)
    @OneToOne(optional = false, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private User deliverer;

    @OneToOne(optional = false, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Warehouse baggage;

    @JsonValue
    public Map<String, Object> toJson() {
        Map<String, Object> result = new HashMap<>();
        result.put("deliveryId", getId());
        result.put("createdAt", this.deliverer.getCreatedAt());

        result.put("name", this.deliverer.getName());
        result.put("baggage", this.baggage.getName());
        result.put("createdBy", this.deliverer.getCreatedBy());
        return result;
    }
}
