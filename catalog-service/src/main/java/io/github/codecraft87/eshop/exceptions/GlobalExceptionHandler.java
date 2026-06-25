package io.github.codecraft87.eshop.exceptions;

import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import io.github.codecraft87.eshop.common.enums.ErrorEnums;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(PaymentCannotBeRetriedException.class)
  public ResponseEntity<ErrorResponse> handlePaymentRetryNotAllowed(
      PaymentCannotBeRetriedException ex) {

    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(
            new ErrorResponse(
                ErrorEnums.PAYMENT_CANNOT_BE_RETRIED,
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage(),
                Instant.now()));
  }

  @ExceptionHandler(ProductNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleProductNotFoundForPaymentException(
      ProductNotFoundException ex) {

    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(
            new ErrorResponse(
                ErrorEnums.PRODUCT_NOT_FOUND,
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage(),
                Instant.now()));
  }

  @ExceptionHandler(PaymentCannotBeCancelledException.class)
  public ResponseEntity<ErrorResponse> handlePaymentCannotBeCancelled(
      PaymentCannotBeCancelledException ex) {

    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(
            new ErrorResponse(
                ErrorEnums.PAYMENT_CANNOT_BE_CANCELLED,
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

  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleResourceNotFoundException(
      ResourceNotFoundException ex) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body(
            new ErrorResponse(
                ErrorEnums.RESOURCE_NOT_FOUND,
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage(),
                Instant.now()));
  }

  @ExceptionHandler(GenericException.class)
  public ResponseEntity<ErrorResponse> handleGenericFoundException(GenericException ex) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body(
            new ErrorResponse(
                ErrorEnums.REGISTRATION_FAILED,
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                ex.getMessage(),
                Instant.now()));
  }
}
