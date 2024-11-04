package petstore.petstore.specifications;

import org.springframework.data.jpa.domain.Specification;
import petstore.petstore.criteria.SearchCriteria;
import petstore.petstore.entities.Product;
import petstore.petstore.enums.SearchOperation;

import java.util.ArrayList;
import java.util.List;

public class ProductSpecificationsBuilder
{
  private List<SearchCriteria> params = new ArrayList<>();

  public ProductSpecificationsBuilder with(String key, String operation, Object value, String prefix, String suffix)
  {
    SearchOperation op = SearchOperation.getSimpleOperation(operation.charAt(0));

    if (op == null)
      return this;

    if (op == SearchOperation.EQUALITY)
    {
      boolean startWithAsterisk = prefix.contains("*");
      boolean endWithAsterisk = suffix.contains("*");

      if (startWithAsterisk && endWithAsterisk)
        op = SearchOperation.CONTAINS;
      else if (startWithAsterisk)
        op = SearchOperation.ENDS_WITH;
      else if (endWithAsterisk)
        op = SearchOperation.STARTS_WITH;
    }

    params.add(new SearchCriteria(key, op, value));
    return this;
  }

  public Specification<Product> build()
  {
    if (params.isEmpty())
      return null;

    Specification result = new ProductSpecification(params.get(0));

    for (int i = 1; i < params.size(); i++)
    {
      result = params.get(i).isOrPredicate()
        ? Specification.where(result).or(new ProductSpecification(params.get(i)))
        : Specification.where(result).and(new ProductSpecification(params.get(i)));
    }

    return result;
  }
}