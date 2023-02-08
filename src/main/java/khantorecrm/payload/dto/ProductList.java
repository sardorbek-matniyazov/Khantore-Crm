package khantorecrm.payload.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductList {
    @NotNull(message = "Product id must not be null")
    private Long productId;
    @NotNull(message = "Amount must not be null")
    private Double amount;
}
