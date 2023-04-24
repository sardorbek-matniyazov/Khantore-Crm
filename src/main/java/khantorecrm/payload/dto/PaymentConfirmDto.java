package khantorecrm.payload.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author :  Sardor Matniyazov
 * @mailto :  sardorbekmatniyazov03@gmail.com
 * @created : 24 Apr 2023
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentConfirmDto {
    private String startDate;
    private String endDate;
    private Long createdById;
}
