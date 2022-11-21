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
    private Long itemOneId;
    @NotNull(message = "Moving second item id is required")
    private Long itemTwoId;
    @NotNull(message = "Moving amount is required")
    private Double amount;
}
