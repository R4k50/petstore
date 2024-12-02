package petstore.petstore.specifications;

import jakarta.persistence.criteria.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import petstore.petstore.criteria.SearchCriteria;
import petstore.petstore.dtos.sectors.SectorDto;
import petstore.petstore.entities.Animal;
import petstore.petstore.entities.AnimalCategory;
import petstore.petstore.entities.Sector;
import petstore.petstore.services.SectorService;

import java.util.Collection;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
public class AnimalSpecification implements Specification<Animal>
{
  private SearchCriteria criteria;

  @Override
  public Predicate toPredicate(Root<Animal> root, CriteriaQuery<?> query, CriteriaBuilder builder)
  {
    return switch(criteria.getOperation())
    {
      case EQUALITY     -> builder.equal(root.get(criteria.getKey()), criteria.getValue());
      case NEGATION     -> builder.notEqual(root.get(criteria.getKey()), criteria.getValue());
      case GREATER_THAN -> builder.greaterThan(root.get(criteria.getKey()), criteria.getValue().toString());
      case LESS_THAN    -> builder.lessThan(root.get(criteria.getKey()), criteria.getValue().toString());
      case LIKE         -> builder.like(root.get(criteria.getKey()), criteria.getValue().toString());
      case STARTS_WITH  -> builder.like(root.get(criteria.getKey()), criteria.getValue() + "%");
      case ENDS_WITH    -> builder.like(root.get(criteria.getKey()), "%" + criteria.getValue());
      case CONTAINS     -> builder.like(root.get(criteria.getKey()), "%" + criteria.getValue() + "%");
      case IN_SET       ->
      {
        if (criteria.getKey().equals("categories"))
        {
          Collection<?> categoryValues = List.of((String) criteria.getValue());

          Join<Animal, AnimalCategory> join = root.join(criteria.getKey());

          Predicate predicate = join.get("name").in(categoryValues);

          yield predicate;
        }

        String sectorName = (String) criteria.getValue();

        Join<Animal, Sector> sectorJoin = root.join("sector");

        Predicate predicate = builder.equal(sectorJoin.get("name"), sectorName);

        yield predicate;
      }
    };
  }
}