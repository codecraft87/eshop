package io.github.codecraft87.eshop.order.exceptions;

import org.springframework.http.HttpStatus;

import io.github.codecraft87.eshop.order.common.enums.ErrorEnums;

public class OrderNotFoundException extends OrderException {
  private static final long serialVersionUID = 1L;

  public OrderNotFoundException(Long id) {
    this.id = id.toString();
  }

  @Override
  protected String getErrorMessage() {
    return String.format(ErrorEnums.ORDER_NOT_FOUND.getErrorMessage(), this.id);
  }

  @Override
  public HttpStatus geHttpStatus() {
    return HttpStatus.NOT_FOUND;
  }

  @Override
  public String getErrorCode() {
    return ErrorEnums.ORDER_NOT_FOUND.getErrorCode();
  }
}
