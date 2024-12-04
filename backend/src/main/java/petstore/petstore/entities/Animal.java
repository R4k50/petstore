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
@Table(name = "animals")
public class Animal
{
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "name", nullable = false)
  @Size(min = 2, max = 50)
  private String name;

  @Column(name = "quantity", nullable = true)
  private int quantity;

  @Column(name = "price", nullable = false)
  @DecimalMin(value = "0.00", inclusive = true)
  @DecimalMax(value = "999.99", inclusive = true)
  @Digits(integer = 3, fraction = 2)
  private BigDecimal price;

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(
      name = "animal_category_map",
      joinColumns = @JoinColumn(name = "animal_id"),
      inverseJoinColumns = @JoinColumn(name = "category_id")
  )
  private Set<AnimalCategory> categories = new HashSet<>();

  @ManyToOne
  @JoinColumn(name = "sector_id")
  private Sector sector;

  @Column(name = "img", nullable = false)
  private String img;
}
