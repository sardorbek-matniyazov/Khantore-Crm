package khantorecrm.payload.dto;

import khantorecrm.utils.aop.CheckFactProductItemList;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FactProductItemDailyDto {
    @CheckFactProductItemList
    private List<FactProductItemDto> productItems;
    @NotBlank(message = "Date is required")
    private String date;
}
