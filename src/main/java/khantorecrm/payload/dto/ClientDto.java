package khantorecrm.payload.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientDto {
    @NotBlank(message = "Name is required")
    private String name;
    @Size(min = 9, max = 9, message = "Phone number must be 9 digits")
    @NotBlank(message = "Phone is required")
    private String phone;
    @NotBlank(message = "Comment is required")
    private String comment;
}
