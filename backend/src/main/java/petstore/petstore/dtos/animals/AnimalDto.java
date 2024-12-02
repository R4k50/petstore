package petstore.petstore.dtos.animals;

import jakarta.persistence.*;
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
public class AnimalDto
{
  @NotNull(message = "Id jest wymagane")
  @Id
  private Long id;

  @NotEmpty(message = "Nazwa jest wymagana")
  @Size(min = 2, max = 50, message = "Nazwa musi mieć od 2 do 50 znaków")
  private String name;

  @NotNull(message = "Ilość jest wymagana")
  private int quantity;

  @NotNull(message = "Cena jest wymagana")
  @DecimalMin(value = "0.00", inclusive = true, message = "Cena musi być liczbą nieujemną")
  @DecimalMax(value = "999.99", inclusive = true, message = "Cena może wynosić maksymalnie 999.99")
  @Digits(integer = 3, fraction = 2, message = "Cena musi mieć dokładnie dwie cyfry po przecinku")
  private BigDecimal price;

  @NotNull(message = "Kategoria jest wymagana")
  private Set<AnimalCategory> categories;

  @NotNull(message = "Sekcja jest wymagana")
  private Sector sector;

  @NotEmpty(message = "Obrazek jest wymagany")
  private String img;
}