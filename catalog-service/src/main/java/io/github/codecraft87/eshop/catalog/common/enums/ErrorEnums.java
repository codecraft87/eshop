package io.github.codecraft87.eshop.catalog.common.enums;

public enum ErrorEnums {
     PRODUCT_NOT_FOUND(
               "PRODUCT_NOT_FOUND",
               "Product [{}] not found in a catalog"),
     VALIDATION_FAILED(
               "VALIDATION_FAILED",
               "Validation failed"),
     DB_NOT_AVAILABLE(
               "DB_NOT_AVAILABLE",
               "Database is currently unavailable. Please try again later."),
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
