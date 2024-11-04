package petstore.petstore.exceptions;

import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

public class ExceptionUtils
{
  public static MethodArgumentNotValidException createValidationException(String field, String errorMessage)
  {
    BindException bindException = new BindException(new Object(), "target");
    bindException.addError(new FieldError("target", field, errorMessage));

    return new MethodArgumentNotValidException(null, bindException.getBindingResult());
  }
}