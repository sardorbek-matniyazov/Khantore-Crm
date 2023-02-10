package khantorecrm.payload.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @author :  Sardor Matniyazov
 * @mailto :  sardorbekmatniyazov03@gmail.com
 * @created : 10 Feb 2023
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SalePaymentDto {
    @NotNull(message = "Sale id is required")
    private Long saleId;
    @NotNull(message = "Amount is required")
    private Double paymentAmount;
}
