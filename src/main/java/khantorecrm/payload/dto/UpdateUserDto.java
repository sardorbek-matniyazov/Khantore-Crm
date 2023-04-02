package khantorecrm.payload.dto;

import khantorecrm.model.enums.RoleName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author :  Sardor Matniyazov
 * @mailto :  sardorbekmatniyazov03@gmail.com
 * @created : 02 Apr 2023
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserDto {
    @NotBlank(message = "Password is required")
    private String password;
    @NotBlank(message = "Name is required")
    private String name;
    @Size(min = 9, max = 9, message = "Phone number must be 9 digits")
    @NotBlank(message = "Phone is required")
    private String phoneNumber;
    @NotNull(message = "Kpi percent value is required")
    private Double kpiPercent;
}
