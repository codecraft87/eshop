package io.github.codecraft87.eshop.security.exception;

import org.springframework.http.HttpStatus;

import io.github.codecraft87.eshop.security.common.ErrorEnums;

public class InvalidJwtTokenException extends SecurityException {

    @Override
    public String getErrorMessage() {
        return ErrorEnums.INVALID_JWT_TOKEN.getErrorMessage();
    }

    @Override
    public HttpStatus geHttpStatus() {
        return HttpStatus.UNAUTHORIZED;
    }

    @Override
    public String getErrorCode() {
        return ErrorEnums.INVALID_JWT_TOKEN.getErrorCode();
    }

}
