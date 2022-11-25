package khantorecrm.model;

import com.fasterxml.jackson.annotation.JsonValue;
import khantorecrm.model.base.BaseEntity;
import khantorecrm.model.enums.PaymentType;
import khantorecrm.utils.constants.NamingConstants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import java.util.HashMap;
import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity(name = "payment")
//caching
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@NoArgsConstructor
@AllArgsConstructor
public class Payment extends BaseEntity {
    @Column(name = "payment_amount")
    private Double amount = 0.0;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_type", length = NamingConstants.MODEL_ENUM_LENGTH)
    private PaymentType type;

    @ManyToOne(optional = false)
    private Balance balance;

    @JsonValue
    public Map<String, Object> toJson() {
        Map<String, Object> result = new HashMap<>();
        result.put("paymentId", super.getId());
        result.put("createdAt", super.getCreatedAt());

        result.put("amount", this.getAmount());
        result.put("type", this.getType());
        result.put("balance", this.getBalance());
        return result;
    }
}
