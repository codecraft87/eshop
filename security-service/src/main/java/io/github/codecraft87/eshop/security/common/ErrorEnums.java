package io.github.codecraft87.eshop.security.common;

public enum ErrorEnums {

    ROLE_NOT_FOUND(
            "ROLE_NOT_FOUND",
            "Role not found {}"),
    REGISTRATION_FAILED(
            "REGISTRATION_FAILED",
            "Registration Failed: User [%s], already exists."),
    INVALID_JWT_TOKEN(
            "INVALID_JWT_TOKEN",
            "Invalid JWT token"),
    DB_NOT_AVAILABLE(
            "DB_NOT_AVAILABLE",
            "Database is currently unavailable. Please try again later.");

    private final String errorCode;
    private final String errorMessage;

    private ErrorEnums(String code, String message) {
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
