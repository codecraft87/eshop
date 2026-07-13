package io.github.codecraft87.eshop.catalog.exceptions;

import org.springframework.http.HttpStatus;

import io.github.codecraft87.eshop.catalog.common.enums.ErrorEnums;

public class ProductNotFoundException extends ProductException {

  private static final long serialVersionUID = 1L;

  public ProductNotFoundException(Long productId) {
    this.id = Long.toString(productId);
  }

  @Override
  public String getErrorMessage() {
    return String.format(ErrorEnums.PRODUCT_NOT_FOUND.getErrorMessage(), this.id);
  }

  @Override
  public HttpStatus geHttpStatus() {
    return HttpStatus.NOT_FOUND;
  }

  @Override
  public String getErrorCode() {
    return ErrorEnums.PRODUCT_NOT_FOUND.getErrorCode();
  }
}
