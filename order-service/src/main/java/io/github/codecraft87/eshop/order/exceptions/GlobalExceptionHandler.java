package io.github.codecraft87.eshop.order.exceptions;

import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import io.github.codecraft87.eshop.order.enums.ErrorEnums;



@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleOrderNotFound(OrderNotFoundException ex) {

      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body(
              new ErrorResponse(
                  ErrorEnums.ORDER_NOT_FOUND,
                  HttpStatus.NOT_FOUND.value(),
                  ex.getMessage(),
                  Instant.now()));
    }

    @ExceptionHandler(OrderAlreadyCancelledException.class)
    public ResponseEntity<ErrorResponse> handleAlreadyCancelled(OrderAlreadyCancelledException ex) {

      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body(
              new ErrorResponse(
                  ErrorEnums.ORDER_ALREADY_CANCELLED,
                  HttpStatus.BAD_REQUEST.value(),
                  ex.getMessage(),
                  Instant.now()));
    }

    @ExceptionHandler(OrderCannotBeModifiedException.class)
    public ResponseEntity<ErrorResponse> handleOrderCannotBeModified(
        OrderCannotBeModifiedException ex) {

      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body(
              new ErrorResponse(
                  ErrorEnums.ORDER_CANNOT_BE_MODIFIED,
                  HttpStatus.BAD_REQUEST.value(),
                  ex.getMessage(),
                  Instant.now()));
    }
    @ExceptionHandler(CancelledOrderCannotBeModifiedException.class)
    public ResponseEntity<ErrorResponse> handleCancelledOrderCannotBeModified(
        CancelledOrderCannotBeModifiedException ex) {

      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body(
              new ErrorResponse(
                  ErrorEnums.CANCELLED_ORDER_CANNOT_BE_MODIFIED,
                  HttpStatus.BAD_REQUEST.value(),
                  ex.getMessage(),
                  Instant.now()));
    }
    
    @ExceptionHandler(InvalidOrderStateForPaymentException.class)
    public ResponseEntity<ErrorResponse> handleInvalidOrderState(
        InvalidOrderStateForPaymentException ex) {

      return ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body(
              new ErrorResponse(
                  ErrorEnums.INVALID_ORDER_STATE,
                  HttpStatus.BAD_REQUEST.value(),
                  ex.getMessage(),
                  Instant.now()));
    }
}
