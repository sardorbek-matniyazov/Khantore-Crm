package khantorecrm.payload.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovingItemDto {
    @NotNull(message = "Moving first item id is required")
    private Long warehouseOneId;
    @NotNull(message = "Moving second item id is required")
    private Long warehouseTwoId;
    @NotNull(message = "Product id is required")
    private Long productId;
    @NotNull(message = "Moving amount is required")
    private Double amount;
}
