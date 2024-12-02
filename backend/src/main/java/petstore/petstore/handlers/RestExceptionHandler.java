package petstore.petstore.handlers;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.validation.FieldError;
import org.springframework.web.multipart.MultipartException;
import petstore.petstore.dtos.errors.ErrorDto;
import petstore.petstore.dtos.errors.ResponseErrorDto;
import petstore.petstore.exceptions.AppException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class RestExceptionHandler
{
  @ExceptionHandler(Exception.class)
  public final ResponseEntity<ErrorDto> handleGeneralExceptions(Exception ex)
  {
    return ResponseEntity
        .status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(new ErrorDto(ex.getMessage()));
  }

  @ExceptionHandler(RuntimeException.class)
  public final ResponseEntity<ErrorDto> handleRuntimeExceptions(RuntimeException ex)
  {
    ex.printStackTrace();

    return ResponseEntity
        .status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(new ErrorDto("Internal server error"));
  }

  @ExceptionHandler(AppException.class)
  public final ResponseEntity<ErrorDto> handleAppExceptions(AppException ex)
  {
    return ResponseEntity
        .status(ex.getStatus())
        .body(new ErrorDto(ex.getMessage()));
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ResponseErrorDto> handleInvalidArgument(MethodArgumentNotValidException ex)
  {
    Map<String, String> errors = ex.getBindingResult().getFieldErrors()
        .stream()
        .collect(Collectors.toMap(
            FieldError::getField,
            FieldError::getDefaultMessage,
            (existing, replacement) -> existing
        ));

    return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body(new ResponseErrorDto(errors));
  }

  @ExceptionHandler(MissingServletRequestPartException.class)
  public final ResponseEntity<ResponseErrorDto> handleMissingServletRequestPartExceptions(MissingServletRequestPartException ex)
  {
    String fieldName = ex.getRequestPartName();
    fieldName = Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);

    Map<String, String> errors = Map.of(ex.getRequestPartName(), "Pole " + fieldName + " jest wymagane");

    return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body(new ResponseErrorDto(errors));
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public final ResponseEntity<ResponseErrorDto> handleHttpMessageNotReadableExceptions(HttpMessageNotReadableException ex)
  {
    String field = "object";
    String fieldName;
    String errorMessage = "jest niepoprawny";

    if (ex.getMostSpecificCause() instanceof InvalidFormatException)
    {
      InvalidFormatException ife = (InvalidFormatException) ex.getMostSpecificCause();
      field = ife.getPath().stream()
        .map(ref -> ref.getFieldName())
        .findFirst()
        .orElse(field);

      errorMessage = "Formatowanie jest niepoprawne";
    }

    fieldName = Character.toUpperCase(field.charAt(0)) + field.substring(1);
    Map<String, String> errors = Map.of(field, fieldName + " " + errorMessage);

    return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body(new ResponseErrorDto(errors));
  }

  @ExceptionHandler(DataIntegrityViolationException.class)
  public final ResponseEntity<ResponseErrorDto> handleDataIntegrityViolationExceptions(DataIntegrityViolationException ex)
  {
    Map<String, String> errors = new HashMap<>();
    errors.put("image", "Obraz jest wymagany");

    return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body(new ResponseErrorDto(errors));
  }

  @ExceptionHandler(MultipartException.class)
  public final ResponseEntity<ResponseErrorDto> handleMultipartExceptions(MultipartException ex)
  {
    Map<String, String> errors = new HashMap<>();
    errors.put("image", "Obraz jest wymagany");

    return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body(new ResponseErrorDto(errors));
  }
}