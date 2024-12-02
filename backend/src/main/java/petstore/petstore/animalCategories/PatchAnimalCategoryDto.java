package petstore.petstore.animalCategories;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PatchAnimalCategoryDto
{
  @Size(min = 2, max = 50, message = "Nazwa musi mieć od 2 do 50 znaków")
  private String name;
}
