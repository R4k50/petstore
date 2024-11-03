package petstore.petstore.dtos.products;

import jakarta.persistence.Id;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDto
{
  @NotEmpty(message = "Id must not be empty")
  @Id
  private Long id;

  @NotEmpty(message = "Name must not be empty")
  @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
  private String name;

  @NotEmpty(message = "Description must not be empty")
  @Size(min = 2, max = 1000, message = "Description must be between 2 and 1000 characters long")
  private String description;

  @NotNull(message = "Price must not be empty")
  @DecimalMin(value = "0.00", inclusive = true, message = "Price must be a non negative number")
  @DecimalMax(value = "999.99", inclusive = true, message = "Price must be at most 999.99")
  @Digits(integer = 3, fraction = 2, message = "Price must have exactly two decimal places")
  private BigDecimal price;

  @NotNull(message = "Quantity must not be empty")
  @DecimalMin(value = "0", inclusive = true, message = "Quantity must be a non negative number")
  private int quantity;

  @NotEmpty(message = "Image path must not be empty")
  private String img;
}
