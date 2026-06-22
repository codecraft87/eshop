package io.github.codecraft87.eshop.exceptions;

public class PaymentCannotBeRetriedException extends RuntimeException {
  private static final long serialVersionUID = 1L;

  public PaymentCannotBeRetriedException(Long id) {
    super("Payment cannot be retried [" + id + "]");
  }
}
