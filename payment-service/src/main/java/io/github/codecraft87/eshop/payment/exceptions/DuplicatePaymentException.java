package io.github.codecraft87.eshop.payment.exceptions;

import org.springframework.http.HttpStatus;

import io.github.codecraft87.eshop.payment.common.enums.ErrorEnums;

public class DuplicatePaymentException extends PaymentException {
  private static final long serialVersionUID = 1L;

  public DuplicatePaymentException(Long id) {
    this.id = id.toString();
  }

  @Override
  protected String getErrorMessage() {
    return String.format(ErrorEnums.DUPLICATE_PAYMENT.getErrorMessage(), this.id);
  }

  @Override
  public HttpStatus geHttpStatus() {
    return HttpStatus.CONFLICT;
  }

  @Override
  public String getErrorCode() {
    return ErrorEnums.DUPLICATE_PAYMENT.getErrorCode();
  }
}
