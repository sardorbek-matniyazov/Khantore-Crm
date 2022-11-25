package khantorecrm.payload.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SaleDto {
    @NotNull(message = "Product item id is required")
    private Long productItemId;
    @NotNull(message = "Product item amount is required")
    private Double amount;
    @NotNull(message = "Client id is required")
    private Long clientId;

    @NotNull(message = "Payment price is required")
    private Double paymentAmount;
}