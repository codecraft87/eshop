package io.github.codecraft87.eshop.catalog.exceptions;

import java.time.Instant;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import io.github.codecraft87.eshop.catalog.common.enums.ErrorEnums;

@RestControllerAdvice
public class GlobalExceptionHandler {

        @ExceptionHandler(ProductException.class)
        public ResponseEntity<ErrorResponse> handleProductNotFoundForPaymentException(
                        ProductException ex) {

                return ResponseEntity.status(ex.geHttpStatus())
                                .body(
                                                new ErrorResponse(
                                                                ex.getErrorCode(),
                                                                ex.geHttpStatus().value(),
                                                                ex.getErrorMessage(),
                                                                Instant.now()));
        }

        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<ErrorResponse> handleValidationException(
                        MethodArgumentNotValidException ex) {

                String errorMessage = ex.getBindingResult().getFieldErrors().stream()
                                .map(error -> "[" + error.getField() + "]: " + error.getDefaultMessage())
                                .findFirst()
                                .orElse(ErrorEnums.VALIDATION_FAILED.getErrorMessage());

                return ResponseEntity.badRequest()
                                .body(
                                                new ErrorResponse(
                                                                ErrorEnums.VALIDATION_FAILED.getErrorCode(),
                                                                HttpStatus.BAD_REQUEST.value(),
                                                                errorMessage,
                                                                Instant.now()));
        }

        @ExceptionHandler(CannotCreateTransactionException.class)
        public ResponseEntity<ErrorResponse> handleDataAccessException(CannotCreateTransactionException e) {
                return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                                .body(new ErrorResponse(
                                                ErrorEnums.DB_NOT_AVAILABLE.getErrorCode(),
                                                HttpStatus.SERVICE_UNAVAILABLE.value(),
                                                ErrorEnums.DB_NOT_AVAILABLE.getErrorMessage(),
                                                Instant.now()));
        }

        @ExceptionHandler(DataAccessException.class)
        public ResponseEntity<ErrorResponse> handleDataAccessException(DataAccessException e) {
                return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                                .body(new ErrorResponse(
                                                ErrorEnums.DB_NOT_AVAILABLE.getErrorCode(),
                                                HttpStatus.SERVICE_UNAVAILABLE.value(),
                                                ErrorEnums.DB_NOT_AVAILABLE.getErrorMessage(),
                                                Instant.now()));
        }
}
