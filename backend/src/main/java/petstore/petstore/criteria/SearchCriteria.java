package petstore.petstore.criteria;

import lombok.*;
import petstore.petstore.enums.SearchOperation;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class SearchCriteria
{
  private String key;
  private SearchOperation operation;
  private Object value;
  @Getter
  private boolean orPredicate;

  public SearchCriteria(String key, SearchOperation operation, Object value)
  {
    this.key = key;
    this.operation = operation;
    this.value = value;
    this.orPredicate = false;
  }
}