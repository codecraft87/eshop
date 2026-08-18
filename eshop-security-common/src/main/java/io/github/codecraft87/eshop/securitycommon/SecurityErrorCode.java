package io.github.codecraft87.eshop.securitycommon;

public enum SecurityErrorCode {

    INVALID_JWT_TOKEN(
            "INVALID_JWT_TOKEN",
            "Invalid JWT token");

    private final String errorCode;

    private final String errorMessage;

    public String getErrorMessage() {
        return errorMessage;
    }

    public String getErrorCode() {
        return errorCode;
    }

    private SecurityErrorCode(String errCode, String errorMessage) {
        this.errorCode = errCode;
        this.errorMessage = errorMessage;
    }
}
