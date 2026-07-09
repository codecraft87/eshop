package io.github.codecraft87.eshop.basket.enums;

public enum ErrorEnums {
  BASKET_NOT_FOUND("BASKET_NOT_FOUND", "Basekt not found"),
  CATALOG_SERVICE_UNAVAILABLE(
      "CATALOG_SERVICE_UNAVAILABLE",
      "Catalog service is currently unavailable. Please try again later."),
  CATALOG_SERVICE_ERROR(
      "CATALOG_SERVICE_ERROR",
      "Catalog service encountered an unexpected error. Please try again later."),
  CATALOG_SERVICE_CLIENT_ERROR(
  "CATALOG_SERVICE_CLIENT_ERROR",
  "Catalog service rejected the request.");

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
