package petstore.petstore.dtos.animals;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import petstore.petstore.entities.AnimalCategory;
import petstore.petstore.entities.Sector;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PatchAnimalDto
{
  @Size(min = 2, max = 50, message = "Nazwa musi mieć od 2 do 50 znaków")
  private String name;

  private int quantity;

  @DecimalMin(value = "0.00", inclusive = true, message = "Cena musi być liczbą nieujemną")
  @DecimalMax(value = "999.99", inclusive = true, message = "Cena może wynosić maksymalnie 999.99")
  @Digits(integer = 3, fraction = 2, message = "Cena musi mieć dokładnie dwie cyfry po przecinku")
  private BigDecimal price;

  private Set<AnimalCategory> categories;

  private Sector sector;

  private String img;
}
