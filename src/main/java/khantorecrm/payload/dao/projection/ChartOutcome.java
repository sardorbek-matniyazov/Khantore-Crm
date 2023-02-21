package khantorecrm.payload.dao.projection;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author :  Sardor Matniyazov
 * @mailto :  sardorbekmatniyazov03@gmail.com
 * @created : 21 Feb 2023
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChartOutcome {
    private String type;
    private double amount;
}
