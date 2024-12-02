package petstore.petstore.animalCategories;

import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AnimalCategoryDto
{
  @NotNull(message = "Id jest wymagane")
  @Id
  private Long id;

  @NotEmpty(message = "Nazwa jest wymagana")
  @Size(min = 2, max = 50, message = "Nazwa musi mieć od 2 do 50 znaków")
  private String name;
}