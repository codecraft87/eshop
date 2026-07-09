package io.github.codecraft87.eshop.basket.exceptions;

import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;

import io.github.codecraft87.eshop.basket.enums.ErrorEnums;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

 
    @ExceptionHandler(BasketNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleBasketNotFoundException(
        BasketNotFoundException ex) {

      return ResponseEntity.status(HttpStatus.NOT_FOUND)
          .body(
              new ErrorResponse(
                  ErrorEnums.BASKET_NOT_FOUND.getErrorCode(),
                  HttpStatus.NOT_FOUND.value(),
                  ErrorEnums.BASKET_NOT_FOUND.getErrorMessage()+
                  " ["+ex.getBasketId()+"]",
                  Instant.now()));
    }

    @ExceptionHandler(ResourceAccessException.class)
    public ResponseEntity<ErrorResponse> handleCatalogServiceUnavaibleException(
        ResourceAccessException ex) {
        log.error("Catalog service unavailable", ex);
      return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
          .body(
              new ErrorResponse(
                  ErrorEnums.CATALOG_SERVICE_UNAVAILABLE.getErrorCode(),
                  HttpStatus.SERVICE_UNAVAILABLE.value(),
                  ErrorEnums.CATALOG_SERVICE_UNAVAILABLE.getErrorMessage(),
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
                  ErrorEnums.CATALOG_SERVICE_CLIENT_ERROR.getErrorMessage(),
                  Instant.now()));
    }
}
