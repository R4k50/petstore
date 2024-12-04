package petstore.petstore.specifications;

import jakarta.persistence.criteria.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import petstore.petstore.criteria.SearchCriteria;
import petstore.petstore.entities.Sector;

@AllArgsConstructor
@NoArgsConstructor
public class SectorSpecification implements Specification<Sector> {
  private SearchCriteria criteria;

  @Override
  public Predicate toPredicate(Root<Sector> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
    return switch(criteria.getOperation()) {
      case EQUALITY -> builder.equal(root.get(criteria.getKey()), criteria.getValue());
      case NEGATION -> builder.notEqual(root.get(criteria.getKey()), criteria.getValue());
      case GREATER_THAN -> builder.greaterThan(root.get(criteria.getKey()), criteria.getValue().toString());
      case LESS_THAN -> builder.lessThan(root.get(criteria.getKey()), criteria.getValue().toString());
      case LIKE -> builder.like(root.get(criteria.getKey()), criteria.getValue().toString());
      case STARTS_WITH -> builder.like(root.get(criteria.getKey()), criteria.getValue() + "%");
      case ENDS_WITH -> builder.like(root.get(criteria.getKey()), "%" + criteria.getValue());
      case CONTAINS -> builder.like(root.get(criteria.getKey()), "%" + criteria.getValue() + "%");
      case IN_SET -> null; // Placeholder: implement logic if needed for set-based operations
    };
  }
}