package khantorecrm.model;

import com.fasterxml.jackson.annotation.JsonValue;
import khantorecrm.model.base.BaseWithCreatedBy;
import khantorecrm.model.enums.PaymentType;
import khantorecrm.utils.constants.Constants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.HashMap;
import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity(name = "payment")
@NoArgsConstructor
@AllArgsConstructor
public class Payment extends BaseWithCreatedBy {
    @Column(name = "payment_amount")
    private Double amount = 0.0;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_type", length = Constants.MODEL_ENUM_LENGTH)
    private PaymentType type;

    @JsonValue
    public Map<String, Object> toJson() {
        Map<String, Object> result = new HashMap<>();
        result.put("paymentId", super.getId());
        result.put("createdAt", super.getCreatedAt());

        result.put("amount", this.getAmount());
        result.put("type", this.getType());
        return result;
    }
}
