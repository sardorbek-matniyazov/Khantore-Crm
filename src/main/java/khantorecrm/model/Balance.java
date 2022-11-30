package khantorecrm.model;

import com.fasterxml.jackson.annotation.JsonValue;
import khantorecrm.model.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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

    @OneToMany(
            cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REMOVE},
            fetch = FetchType.LAZY
    )
    @JoinColumn(name = "balance_fk", referencedColumnName = "id")
    private Set<Payment> payments = new HashSet<>();

    @JsonValue
    public Map<String, Object> toJson() {
        Map<String, Object> result = new HashMap<>();
        result.put("balanceId", super.getId());
        result.put("createdAt", super.getCreatedAt());

        result.put("amount", this.getAmount());
        return result;
    }
}
