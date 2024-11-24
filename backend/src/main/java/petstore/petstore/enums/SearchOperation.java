package petstore.petstore.enums;

public enum SearchOperation {
  EQUALITY,
  NEGATION,
  GREATER_THAN,
  LESS_THAN,
  LIKE,
  STARTS_WITH,
  ENDS_WITH,
  CONTAINS,
  IN_SET;

  public static final String[] SIMPLE_OPERATION_SET = { ":", "!", ">", "<", "~", ";" };

  public static SearchOperation getSimpleOperation(char input)
  {
    return switch(input)
    {
      case ':' -> EQUALITY;
      case '!' -> NEGATION;
      case '>' -> GREATER_THAN;
      case '<' -> LESS_THAN;
      case '~' -> LIKE;
      case ';' -> IN_SET;
      default  -> null;
    };
  }
}