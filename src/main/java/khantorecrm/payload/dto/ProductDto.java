package khantorecrm.payload.dto;

import khantorecrm.model.Product;
import khantorecrm.model.enums.ProductType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    @NotBlank(message = "Name must not be empty")
    private String name;
    @NotNull(message = "Type must not be null")
    private ProductType type;
    @NotNull(message = "Price must not be null")
    private Double price;

    // optional, These are only for product with type PRODUCT
    private List<ProductItemList> ingredients;

    public Product toEntity() {
        return new Product(
                name,
                price,
                type
        );
    }
}
