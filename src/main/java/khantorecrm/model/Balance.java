package khantorecrm.model;

import com.fasterxml.jackson.annotation.JsonValue;
import khantorecrm.model.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.util.HashMap;
import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity(name = "balance")
//caching
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@NoArgsConstructor
@AllArgsConstructor
public class Balance extends BaseEntity {
    @Column(name = "balance_amount")
    private Double amount = 0.0;

    @JsonValue
    public Map<String, Object> toJson() {
        Map<String, Object> result = new HashMap<>();
        result.put("balanceId", super.getId());
        result.put("createdAt", super.getCreatedAt());

        result.put("amount", this.getAmount());
        return result;
    }
}
