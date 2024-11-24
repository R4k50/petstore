package petstore.petstore.dtos.products;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import petstore.petstore.entities.ProductCategory;

import java.math.BigDecimal;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PatchProductDto
{
  @Size(min = 2, max = 50, message = "Nazwa musi mieć od 2 do 50 znaków")
  private String name;

  @Size(min = 2, max = 1000, message = "Opis musi mieć od 2 do 1000 znaków")
  private String description;

  @DecimalMin(value = "0.00", inclusive = true, message = "Cena musi być liczbą nieujemną")
  @DecimalMax(value = "999.99", inclusive = true, message = "Cena może wynosić maksymalnie 999.99")
  @Digits(integer = 3, fraction = 2, message = "Cena musi mieć dokładnie dwie cyfry po przecinku")
  private BigDecimal price;

  private Set<ProductCategory> categories;

  @DecimalMin(value = "0", inclusive = true, message = "Ilość musi być liczbą nieujemną")
  private int quantity;

  private String img;
}
