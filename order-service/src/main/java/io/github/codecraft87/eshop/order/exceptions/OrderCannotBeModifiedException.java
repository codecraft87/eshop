package io.github.codecraft87.eshop.order.exceptions;

import org.springframework.http.HttpStatus;

import io.github.codecraft87.eshop.order.common.enums.ErrorEnums;

public class OrderCannotBeModifiedException extends OrderException {
  private static final long serialVersionUID = 1L;

  public OrderCannotBeModifiedException(Long orderId) {
    this.id = orderId.toString();
  }

  @Override
  protected String getErrorMessage() {
    return String.format(ErrorEnums.ORDER_CANNOT_BE_MODIFIED.getErrorMessage(), this.id);
  }

  @Override
  public HttpStatus geHttpStatus() {
    return HttpStatus.BAD_REQUEST;
  }

  @Override
  public String getErrorCode() {
    return ErrorEnums.ORDER_CANNOT_BE_MODIFIED.getErrorCode();
  }
}
