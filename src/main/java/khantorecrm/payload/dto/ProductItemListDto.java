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

    @NotNull(message = "Product item amount must not be null")
    private Double amount;

    @JsonIgnore
    public boolean isItemCreatableId() {
        return productItemId != null && productItemId > 0;
    }
}
