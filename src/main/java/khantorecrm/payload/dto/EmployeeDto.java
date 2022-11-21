package khantorecrm.payload.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDto {
    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "PhoneNumber is required")
    private String phoneNumber;

    @NotBlank(message = "Comment is required")
    private String comment;
}
