package khantorecrm.payload.dto;

import khantorecrm.utils.aop.CheckFactProductItemList;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FactProductItemDailyDto {
    @CheckFactProductItemList
    private List<FactProductItemDto> productItems;
    private String date;
}
