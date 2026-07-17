package io.github.codecraft87.eshop.security.exception;

import org.springframework.http.HttpStatus;

import io.github.codecraft87.eshop.security.common.ErrorEnums;

public class RoleNotFoundException extends SecurityException {

    public RoleNotFoundException(String roleName) {
        super();
        this.id = roleName;
    }

    @Override
    public HttpStatus geHttpStatus() {
        return HttpStatus.NOT_FOUND;
    }

    @Override
    public String getErrorCode() {
        return ErrorEnums.ROLE_NOT_FOUND.getErrorCode();
    }

    @Override
    public String getErrorMessage() {
        return String.format(ErrorEnums.ROLE_NOT_FOUND.getErrorMessage(), this.id);
    }
}