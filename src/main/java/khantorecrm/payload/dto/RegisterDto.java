package khantorecrm.payload.dto;

import khantorecrm.model.enums.RoleName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterDto {
    @NotBlank(message = "Password is required")
    private String password;
    @NotBlank(message = "Name is required")
    private String name;
    @NotBlank(message = "Phone number is required")
    private String phoneNumber;
    @NotNull(message = "Role name is required")
    private RoleName roleName;
    @NotNull(message = "Kpi percent value is required")
    private Double kpiPercent;
}
