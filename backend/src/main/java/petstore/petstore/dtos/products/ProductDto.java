package petstore.petstore.dtos.products;

import jakarta.persistence.Id;
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
public class ProductDto
{
  @NotNull(message = "Id jest wymagane")
  @Id
  private Long id;

  @NotEmpty(message = "Nazwa jest wymagana")
  @Size(min = 2, max = 50, message = "Nazwa musi mieć od 2 do 50 znaków")
  private String name;

  @NotEmpty(message = "Opis jest wymagany")
  @Size(min = 2, max = 1000, message = "Opis musi mieć od 2 do 1000 znaków")
  private String description;

  @NotNull(message = "Cena jest wymagana")
  @DecimalMin(value = "0.00", inclusive = true, message = "Cena musi być liczbą nieujemną")
  @DecimalMax(value = "999.99", inclusive = true, message = "Cena może wynosić maksymalnie 999.99")
  @Digits(integer = 3, fraction = 2, message = "Cena musi mieć dokładnie dwie cyfry po przecinku")
  private BigDecimal price;

  @NotNull(message = "Ilość jest wymagana")
  @DecimalMin(value = "0", inclusive = true, message = "Ilość musi być liczbą nieujemną")
  private int quantity;

  @NotNull(message = "Kategoria jest wymagana")
  private Set<ProductCategory> categories;

  @NotEmpty(message = "Obrazek jest wymagany")
  private String img;
}
