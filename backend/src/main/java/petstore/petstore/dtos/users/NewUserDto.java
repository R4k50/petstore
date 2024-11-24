package petstore.petstore.dtos.users;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NewUserDto
{
    @NotEmpty(message = "Imię nie może być puste")
    @Size(min = 2, max = 50, message = "Imię musi mieć od 2 do 50 znaków")
    private String name;

    @NotEmpty(message = "Nazwisko nie może być puste")
    @Size(min = 2, max = 50, message = "Nazwisko musi mieć od 2 do 50 znaków")
    private String surname;

    @NotEmpty(message = "Email nie może być pusty")
    @Size(min = 2, max = 50, message = "Email musi mieć od 2 do 50 znaków")
    @Email(message = "Nieprawidłowy email")
    private String email;

    @NotEmpty(message = "Hasło nie może być puste")
    @Size(min = 8, max = 100, message = "Hasło musi mieć od 8 do 100 znaków")
    private String password;
}
