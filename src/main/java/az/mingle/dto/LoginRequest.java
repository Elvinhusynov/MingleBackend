package az.mingle.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LoginRequest {

    @NotBlank(message = "Username can not be blank")
    private String username;

    @NotBlank(message = "Password can not be blank")
    private String password;
}
