package io.github.codecraft87.eshop.security.exception;

import java.time.Instant;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class SecurityGlobalExceptionHandler {

    @ExceptionHandler(SecurityException.class)
    public ResponseEntity<ErrorResponse> handleRoleNotFoundException(
            SecurityException ex) {
        return buildResponseEntity(ex);
    }

    private ResponseEntity<ErrorResponse> buildResponseEntity(SecurityException ex) {
        return ResponseEntity.status(ex.geHttpStatus())
                .body(
                        new ErrorResponse(
                                ex.getErrorCode(),
                                ex.geHttpStatus().value(),
                                ex.getErrorMessage(),
                                Instant.now()));
    }
}
