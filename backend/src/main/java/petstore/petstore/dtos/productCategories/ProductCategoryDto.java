package petstore.petstore.dtos.productCategories;

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
public class ProductCategoryDto
{
  @NotNull(message = "Id jest wymagane")
  @Id
  private Long id;

  @NotEmpty(message = "Imię jest wymagane")
  @Size(min = 2, max = 50, message = "Imię musi mieć od 2 do 50 znaków")
  private String name;
}
