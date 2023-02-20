package khantorecrm.payload.dto;

import khantorecrm.model.enums.RoleName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterDto {
    @NotBlank(message = "Password is required")
    private String password;
    @NotBlank(message = "Name is required")
    private String name;
    @Size(min = 9, max = 9, message = "Phone number must be 9 digits")
    @NotBlank(message = "Phone is required")
    private String phoneNumber;
    @NotNull(message = "Role name is required")
    private RoleName roleName;
    @NotNull(message = "Kpi percent value is required")
    private Double kpiPercent;
}
