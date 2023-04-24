package khantorecrm.model;

import com.fasterxml.jackson.annotation.JsonValue;
import khantorecrm.model.base.BaseWithCreatedBy;
import khantorecrm.model.enums.PaymentOrderType;
import khantorecrm.model.enums.PaymentStatus;
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

import static khantorecrm.model.enums.PaymentOrderType.NEW;
import static khantorecrm.model.enums.PaymentType.CASH;

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
    private PaymentType type = CASH;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", length = Constants.MODEL_ENUM_LENGTH)
    private PaymentStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_order_type", length = Constants.MODEL_ENUM_LENGTH)
    private PaymentOrderType orderType = NEW;

    public Payment(Double amount, PaymentType type, PaymentStatus status) {
        this.amount = amount;
        this.type = type;
        this.status = status;
    }

    @JsonValue
    public Map<String, Object> toJson() {
        Map<String, Object> result = new HashMap<>();
        result.put("paymentId", super.getId());
        result.put("createdAt", super.getCreatedAt());

        result.put("amount", this.getAmount());
        result.put("type", this.getType());
        result.put("status", this.getStatus());
        result.put("orderType", this.getOrderType());
        return result;
    }
}
