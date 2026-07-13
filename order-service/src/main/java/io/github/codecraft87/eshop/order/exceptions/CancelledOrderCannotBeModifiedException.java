package io.github.codecraft87.eshop.order.exceptions;

import org.springframework.http.HttpStatus;

import io.github.codecraft87.eshop.order.enums.ErrorEnums;

public class CancelledOrderCannotBeModifiedException extends OrderException {
  private static final long serialVersionUID = 1L;

  public CancelledOrderCannotBeModifiedException(Long orderId) {
    this.id = orderId.toString();
  }

  @Override
  protected String getErrorMessage() {
    return String.format(ErrorEnums.CANCELLED_ORDER_CANNOT_BE_MODIFIED.getErrorMessage(), this.id);
  }

  @Override
  public HttpStatus geHttpStatus() {
    return HttpStatus.BAD_REQUEST;
  }

  @Override
  public String getErrorCode() {
    return ErrorEnums.CANCELLED_ORDER_CANNOT_BE_MODIFIED.getErrorCode();
  }
}
