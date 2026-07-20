package io.github.codecraft87.eshop.order.exceptions;

import org.springframework.http.HttpStatus;

import io.github.codecraft87.eshop.order.common.enums.ErrorEnums;

public class InvalidOrderStateForPaymentException extends OrderException {
  private static final long serialVersionUID = 1L;

  public InvalidOrderStateForPaymentException(Long id) {
    this.id = id.toString();
  }

  @Override
  protected String getErrorMessage() {
    return String.format(ErrorEnums.INVALID_ORDER_STATE.getErrorMessage(), this.id);
  }

  @Override
  public HttpStatus geHttpStatus() {
    return HttpStatus.BAD_REQUEST;
  }

  @Override
  public String getErrorCode() {
    return ErrorEnums.INVALID_ORDER_STATE.getErrorCode();
  }

}
