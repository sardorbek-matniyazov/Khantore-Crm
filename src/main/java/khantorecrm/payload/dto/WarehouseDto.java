package khantorecrm.payload.dto;

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
public class WarehouseDto {
    @NotBlank(message = "Name must not be empty")
    private String name;

    @NotNull(message = "Product item list must not be empty")
    private List<ProductList> productList;

    @NotNull(message = "Warehouse type must not be empty")
    private ProductType type;
}
