package io.github.codecraft87.eshop.order.exceptions;

import java.time.Instant;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import io.github.codecraft87.eshop.order.common.enums.ErrorEnums;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(OrderException.class)
    public ResponseEntity<ErrorResponse> handleOrderNotFound(OrderException ex) {
        log.info("********* Handled by [{}] exception ", ex.getClass().getName());
        return buildResponseEntinty(ex);
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ErrorResponse> handleDataAccessException(DataAccessException e) {
        log.info("******* Handled by DataAccessException");

        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(new ErrorResponse(
                        ErrorEnums.DB_NOT_AVAILABLE.getErrorCode(),
                        HttpStatus.SERVICE_UNAVAILABLE.value(),
                        ErrorEnums.DB_NOT_AVAILABLE.getErrorMessage(),
                        Instant.now()));
    }

    @ExceptionHandler(CannotCreateTransactionException.class)
    public ResponseEntity<ErrorResponse> handleDataAccessException(CannotCreateTransactionException e) {
        log.info("******* Handled by CannotCreateTransactionException");
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(new ErrorResponse(
                        ErrorEnums.DB_NOT_AVAILABLE.getErrorCode(),
                        HttpStatus.SERVICE_UNAVAILABLE.value(),
                        ErrorEnums.DB_NOT_AVAILABLE.getErrorMessage(),
                        Instant.now()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex) {
        log.error("Caught by generic handler", ex);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(
                        "UNEXPECTED",
                        500,
                        ex.getClass().getSimpleName(),
                        Instant.now()));
    }

    private ResponseEntity<ErrorResponse> buildResponseEntinty(OrderException ex) {
        return ResponseEntity.status(ex.geHttpStatus())
                .body(
                        new ErrorResponse(
                                ex.getErrorCode(),
                                ex.geHttpStatus().value(),
                                ex.getErrorMessage(),
                                Instant.now()));
    }

}
