package khantorecrm.payload.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author :  Sardor Matniyazov
 * @mailto :  sardorbekmatniyazov03@gmail.com
 * @created : 08 Feb 2023
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductPriceForSellerDto {
    @NotNull(message = "Delivery id is required!")
    private Long delivererId;
    private List<ProductList> productList;
}
