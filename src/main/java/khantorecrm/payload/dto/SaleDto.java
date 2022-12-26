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
public class SaleDto {
    @CheckProductItemList
    private List<ProductItemListDto> productItemsList;

    @NotNull(message = "Client id is required")
    private Long clientId;

    @NotNull(message = "Payment price is required")
    private Double paymentAmount;
}