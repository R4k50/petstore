package petstore.petstore.dtos.sectors;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import petstore.petstore.entities.Animal;

import java.util.Date;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NewSectorDto
{
  @NotEmpty(message = "Imię jest wymagane")
  @Size(min = 2, max = 50, message = "Imię musi mieć od 2 do 50 znaków")
  private String name;

  @Size(max = 5000)
  private String description;

  private Date lastCare;

  private Date lastFeed;

  private Set<Animal> animals;
}
