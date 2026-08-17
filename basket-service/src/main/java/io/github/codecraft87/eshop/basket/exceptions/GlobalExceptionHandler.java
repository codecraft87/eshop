package io.github.codecraft87.eshop.basket.exceptions;

import java.time.Instant;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;

import io.github.codecraft87.eshop.basket.common.enums.ErrorEnums;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

        @ExceptionHandler(BasketException.class)
        public ResponseEntity<ErrorResponse> handleBasketNotFoundException(
                        BasketException ex) {
                return ResponseEntity.status(ex.geHttpStatus())
                                .body(
                                                new ErrorResponse(
                                                                ex.getErrorCode(),
                                                                ex.geHttpStatus().value(),
                                                                ex.getErrorMessage(),
                                                                Instant.now()));
        }

        @ExceptionHandler(ResourceAccessException.class)
        public ResponseEntity<ErrorResponse> handleCatalogServiceUnavaibleException(
                        ResourceAccessException ex) {

                return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                                .body(
                                                new ErrorResponse(
                                                                ErrorEnums.CATALOG_SERVICE_UNAVAILABLE.getErrorCode(),
                                                                HttpStatus.SERVICE_UNAVAILABLE.value(),
                                                                ErrorEnums.CATALOG_SERVICE_UNAVAILABLE
                                                                                .getErrorMessage(),
                                                                Instant.now()));
        }

        @ExceptionHandler(HttpServerErrorException.class)
        public ResponseEntity<ErrorResponse> handleHttpServerErrorException(
                        HttpServerErrorException ex) {
                return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                                .body(
                                                new ErrorResponse(
                                                                ErrorEnums.CATALOG_SERVICE_ERROR.getErrorCode(),
                                                                HttpStatus.BAD_GATEWAY.value(),
                                                                ErrorEnums.CATALOG_SERVICE_ERROR.getErrorMessage(),
                                                                Instant.now()));
        }

        @ExceptionHandler(HttpClientErrorException.class)
        public ResponseEntity<ErrorResponse> handleHttpClientErrorException(
                        HttpClientErrorException ex) {
                return ResponseEntity.status(ex.getStatusCode())
                                .body(
                                                new ErrorResponse(
                                                                ErrorEnums.CATALOG_SERVICE_CLIENT_ERROR.getErrorCode(),
                                                                ex.getStatusCode().value(),
                                                                ErrorEnums.CATALOG_SERVICE_CLIENT_ERROR
                                                                                .getErrorMessage(),
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

        @ExceptionHandler(CannotCreateTransactionException.class)
        public ResponseEntity<ErrorResponse> handleDataAccessException(CannotCreateTransactionException e) {
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
}
