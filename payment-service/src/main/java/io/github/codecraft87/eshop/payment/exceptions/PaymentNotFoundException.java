package io.github.codecraft87.eshop.payment.exceptions;

import org.springframework.http.HttpStatus;

import io.github.codecraft87.eshop.payment.enums.ErrorEnums;

public class PaymentNotFoundException extends PaymentException {
  private static final long serialVersionUID = 1L;

  public PaymentNotFoundException(Long id) {
    this.id = id.toString();
  }

  @Override
  protected String getErrorMessage() {
    return String.format(ErrorEnums.PAYMENT_NOT_FOUND.getErrorMessage(), this.id);
  }

  @Override
  public HttpStatus geHttpStatus() {
    return HttpStatus.NOT_FOUND;
  }

  @Override
  public String getErrorCode() {
    return ErrorEnums.PAYMENT_NOT_FOUND.getErrorCode();
  }
}
