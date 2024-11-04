package petstore.petstore.dtos.errors;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@AllArgsConstructor
@Data
public class ResponseErrorDto
{
  private Map<String, String> errors;
}