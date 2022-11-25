package khantorecrm.payload.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryDto {
    @NotNull(message = "Delivery id is required")
    private Long deliveryId;
    @NotNull(message = "Product item id is required")
    private Long productItemId;
    @NotNull(message = "Product item amount is required")
    private Double amount;
}
