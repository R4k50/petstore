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
    @NotEmpty(message = "Adres email jest wymagany")
    @Email(message = "Nieprawidłowy adres email")
    private String email;

    @NotEmpty(message = "Hasło jest wymagane")
    private String password;
}