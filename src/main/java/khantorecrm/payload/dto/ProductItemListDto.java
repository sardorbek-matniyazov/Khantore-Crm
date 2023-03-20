package khantorecrm.payload.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductItemListDto {
    @NotNull(message = "Product item id must not be null")
    private Long productItemId;

    private Double amount;

    @NotNull(message = "howMuchIngredient must not be null")
    private Double howMuchIngredient;

    @NotNull(message = "forHowMuchProduct must not be null")
    private Double forHowMuchProduct;

    @JsonIgnore
    public boolean isItemCreatableId() {
        return productItemId != null && productItemId > 0;
    }
}
