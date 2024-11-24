package petstore.petstore.dtos.products;

import com.fasterxml.jackson.annotation.JsonProperty;
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
public class NewProductDto
{
  @NotEmpty(message = "Name is required")
  @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
  private String name;

  @NotEmpty(message = "Description is required")
  @Size(min = 2, max = 1000, message = "Description must be between 2 and 1000 characters long")
  private String description;

  @NotNull(message = "Price is required")
  @DecimalMin(value = "0.00", inclusive = true, message = "Price must be a non-negative number")
  @DecimalMax(value = "999.99", inclusive = true, message = "Price must be at most 999.99")
  @Digits(integer = 3, fraction = 2, message = "Price must have exactly two decimal places")
  private BigDecimal price;

  @NotNull(message = "Quantity is required")
  @DecimalMin(value = "0", inclusive = true, message = "Quantity must be a non-negative number")
  private int quantity;

  @NotNull(message = "Category is required")
  private Set<ProductCategory> categories;

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  private String img;
}
