package petstore.petstore.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
@Table(name = "products")
public class Product
{
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "name", nullable = false)
  @Size(min = 2, max = 50)
  private String name;

  @Column(name = "description", nullable = false)
  @Size(min = 2, max = 1000)
  private String description;

  @Column(name = "price", nullable = false)
  @DecimalMin(value = "0.00", inclusive = true)
  @DecimalMax(value = "999.99", inclusive = true)
  @Digits(integer = 3, fraction = 2)
  private BigDecimal price;

  @Column(name = "quantity", nullable = false)
  @DecimalMin(value = "0")
  private int quantity;

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(
      name = "product_category_map",
      joinColumns = @JoinColumn(name = "product_id"),
      inverseJoinColumns = @JoinColumn(name = "category_id")
  )
  private Set<ProductCategory> categories = new HashSet<>();

  @Column(name = "img", nullable = false)
  private String img;
}
