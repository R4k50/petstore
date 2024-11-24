package petstore.petstore.dtos.users;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PatchUserDto
{
    @Size(min = 2, max = 50, message = "Imię musi mieć od 2 do 50 znaków")
    private String name;

    @Size(min = 2, max = 50, message = "Nazwisko musi mieć od 2 do 50 znaków")
    private String surname;

    @Size(min = 2, max = 50, message = "Email musi mieć od 2 do 50 znaków")
    @Email(message = "Nieprawidłowy email")
    private String email;

    @Size(min = 8, max = 100, message = "Hasło musi mieć od 8 do 100 znaków")
    private String password;
}
