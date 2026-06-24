package io.github.codecraft87.eshop.basket.exceptions;

import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import io.github.codecraft87.eshop.basket.enums.ErrorEnums;

@RestControllerAdvice
public class GlobalExceptionHandler {

 
    @ExceptionHandler(BasketNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleBasketNotFoundException(
        BasketNotFoundException ex) {

      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body(
              new ErrorResponse(
                  ErrorEnums.BASKET_NOT_FOUND,
                  HttpStatus.BAD_REQUEST.value(),
                  ex.getMessage(),
                  Instant.now()));
    }
    
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleGeneralException(Exception ex) {

    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(
            new ErrorResponse(
                ErrorEnums.INTERNAL_SERVER_ERROR,
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                ex.getMessage(),
                Instant.now()));
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleValidationException(
      MethodArgumentNotValidException ex) {

    String errorMessage =
        ex.getBindingResult().getFieldErrors().stream()
            .map(error -> "[" + error.getField() + "]: " + error.getDefaultMessage())
            .findFirst()
            .orElse("Validation failed");

    return ResponseEntity.badRequest()
        .body(
            new ErrorResponse(
                ErrorEnums.VALIDATION_FAILED,
                HttpStatus.BAD_REQUEST.value(),
                errorMessage,
                Instant.now()));
  }
}
