package khantorecrm.payload.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientDto {
    @NotBlank(message = "Name is required")
    private String name;
    @NotBlank(message = "Phone is required")
    private String phone;
    @NotBlank(message = "Comment is required")
    private String comment;
}
