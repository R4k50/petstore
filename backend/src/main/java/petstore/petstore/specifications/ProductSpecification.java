package petstore.petstore.specifications;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import jakarta.persistence.metamodel.EntityType;
import jakarta.persistence.metamodel.Metamodel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import petstore.petstore.criteria.SearchCriteria;
import petstore.petstore.entities.Product;
import petstore.petstore.entities.ProductCategory;

import java.util.Collection;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
public class ProductSpecification implements Specification<Product>
{
  private SearchCriteria criteria;
  @Override
  public Predicate toPredicate(Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder builder)
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
      case IN_SET       -> {
        Collection<?> categoryValues = List.of((String) criteria.getValue());

        Join<Product, ProductCategory> join = root.join("categories");

        Predicate categoryPredicate = join.get("name").in(categoryValues);

        yield categoryPredicate;
      }
    };
  }
}
