package petstore.petstore.dtos.errors;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ErrorDto
{
  private String error;
}
