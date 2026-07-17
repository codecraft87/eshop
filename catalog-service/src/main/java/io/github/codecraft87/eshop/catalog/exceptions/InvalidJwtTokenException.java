package io.github.codecraft87.eshop.catalog.exceptions;

import org.springframework.http.HttpStatus;

import io.github.codecraft87.eshop.catalog.common.enums.ErrorEnums;

public class InvalidJwtTokenException extends RuntimeException {

    public String getErrorMessage() {
        return ErrorEnums.INVALID_JWT_TOKEN.getErrorMessage();
    }

    public HttpStatus geHttpStatus() {
        return HttpStatus.UNAUTHORIZED;
    }

    public String getErrorCode() {
        return ErrorEnums.INVALID_JWT_TOKEN.getErrorCode();
    }
}
