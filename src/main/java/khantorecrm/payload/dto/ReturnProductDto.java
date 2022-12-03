package khantorecrm.payload.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReturnProductDto {
    // todo: skladqa otkeriw problemasi
    @NotNull(message = "Returned Product item id must not be null")
    private Long returnedProductItemId;

    @NotNull(message = "Recipient Product item id must not be null")
    private Long recipientProductItemId;

    @NotNull(message = "Product item amount must not be null")
    private Double amount;
}
