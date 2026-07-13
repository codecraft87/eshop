package io.github.codecraft87.eshop.payment.enums;

public enum ErrorEnums {
  DUPLICATE_PAYMENT(
      "DUPLICATE_PAYMENT",
      "Duplicate payment detected for order [{}]"),
  PAYMENT_NOT_FOUND(
      "PAYMENT_NOT_FOUND",
      "Invalid payment id[{}]"),
  DB_NOT_AVAILABLE(
      "DB_NOT_AVAILABLE",
      "Database is currently unavailable. Please try again later.");

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
