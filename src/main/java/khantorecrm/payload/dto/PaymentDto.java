package khantorecrm.payload.dto;

import khantorecrm.model.enums.PaymentType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDto {
    @NotNull(message = "Payment amount is required")
    private Double amount;
    @NotNull(message = "Payment type is required")
    private PaymentType type;
}
