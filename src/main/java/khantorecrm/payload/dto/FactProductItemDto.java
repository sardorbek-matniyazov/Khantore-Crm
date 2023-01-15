package khantorecrm.payload.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FactProductItemDto {
    private Long productItemId;
    private Double startAmount;
    private Double endAmount;
}
