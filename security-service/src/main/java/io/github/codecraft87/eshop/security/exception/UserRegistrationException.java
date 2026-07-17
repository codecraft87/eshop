package io.github.codecraft87.eshop.security.exception;

import org.springframework.http.HttpStatus;

import io.github.codecraft87.eshop.security.common.ErrorEnums;

public class UserRegistrationException extends SecurityException {

    public UserRegistrationException(String userName) {
        this.id = userName;
    }

    @Override
    protected String getErrorMessage() {
        return String.format(ErrorEnums.REGISTRATION_FAILED.getErrorMessage(), this.id);
    }

    @Override
    public HttpStatus geHttpStatus() {
        return HttpStatus.BAD_REQUEST;
    }

    @Override
    public String getErrorCode() {
        return ErrorEnums.REGISTRATION_FAILED.getErrorCode();
    }
}
