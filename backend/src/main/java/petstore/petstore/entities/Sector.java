package petstore.petstore.entities;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
@Table(name = "sectors")
public class Sector
{
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "name", nullable = false)
  @Size(min = 2, max = 50)
  private String name;

  @Column(name = "description")
  @Size(max = 5000)
  private String description;

  @Column(name = "last_care")
  private Date lastCare;

  @Column(name = "last_feed")
  private Date lastFeed;

  @OneToMany(mappedBy = "sector", fetch = FetchType.LAZY)
  private Set<Animal> animals;
}
