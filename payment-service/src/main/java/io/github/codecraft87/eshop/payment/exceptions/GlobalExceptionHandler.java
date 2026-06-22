package io.github.codecraft87.eshop.payment.exceptions;

import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import io.github.codecraft87.eshop.payment.enums.ErrorEnums;



@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(DuplicatePaymentException.class)
    public ResponseEntity<ErrorResponse> handleDuplicatePayment(DuplicatePaymentException ex) {

      return ResponseEntity.status(HttpStatus.CONFLICT)
           .body(
              new ErrorResponse(
                  ErrorEnums.DUPLICATE_PAYMENT,
                  HttpStatus.CONFLICT.value(),
                  ex.getMessage(),
                  Instant.now()));
    }
    
    @ExceptionHandler(PaymentNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlePaymentNotFound(PaymentNotFoundException ex) {

      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body(
              new ErrorResponse(
                  ErrorEnums.PAYMENT_NOT_FOUND,
                  HttpStatus.NOT_FOUND.value(),
                  ex.getMessage(),
                  Instant.now()));
    }
}
