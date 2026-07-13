package io.github.codecraft87.eshop.security.common;

public enum ErrorNums {

    ROLE_NOT_FOUND("ROLE_NOT_FOUND", "Role not found {}"),
    REGISTRATION_FAILED("REGISTRATION_FAILED", "Registration Failed: User [{}], already exists.");

    private final String errorCode;
    private final String errorMessage;

    private ErrorNums(String code, String message) {
        this.errorCode = code;
        this.errorMessage = message;
    }

    public String getErrorCode() {
        return this.errorCode;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

}
