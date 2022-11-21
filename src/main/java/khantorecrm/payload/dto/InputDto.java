package khantorecrm.payload.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InputDto {
    @NotNull(message = "Product id is required")
    private Long productItemId;
    @NotNull(message = "Product amount is required")
    private Double amount;
    @NotNull(message = "Product price is required")
    private Double price;
    @NotNull(message = "Employer id is required")
    private Long employerId;
}
