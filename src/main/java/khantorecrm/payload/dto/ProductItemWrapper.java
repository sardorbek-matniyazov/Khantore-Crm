package khantorecrm.payload.dto;

import khantorecrm.utils.aop.CheckProductItemList;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductItemWrapper {
    @CheckProductItemList
    private List<ProductItemListDto> items;
}
