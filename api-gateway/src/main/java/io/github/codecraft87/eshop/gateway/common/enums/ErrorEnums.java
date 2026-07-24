package io.github.codecraft87.eshop.gateway.common.enums;

public enum ErrorEnums {

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

  private ErrorEnums(String errCode, String errorMessage) {
    this.errorCode = errCode;
    this.errorMessage = errorMessage;
  }
}
