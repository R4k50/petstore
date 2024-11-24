package petstore.petstore.dtos.productCategories;

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
public class NewProductCategoryDto
{
  @NotEmpty(message = "Imię jest wymagane")
  @Size(min = 2, max = 50, message = "Imię musi mieć od 2 do 50 znaków")
  private String name;
}
