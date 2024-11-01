package petstore.petstore.dtos.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginDto
{
    @NotEmpty(message = "Email must not be empty")
    @Email(message = "Invalid email")
    private String email;

    @NotEmpty(message = "Password must not be empty")
    private String password;
}