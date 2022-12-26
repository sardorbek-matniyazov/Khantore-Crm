package khantorecrm.payload.dto;

import khantorecrm.utils.aop.CheckProductItemList;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryDto {
    @NotNull(message = "Delivery id is required")
    private Long deliveryId;
    @CheckProductItemList
    private List<ProductItemListDto> items;
}
