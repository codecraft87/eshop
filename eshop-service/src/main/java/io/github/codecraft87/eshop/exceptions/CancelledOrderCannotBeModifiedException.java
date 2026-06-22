package io.github.codecraft87.eshop.exceptions;

public class CancelledOrderCannotBeModifiedException extends RuntimeException {
  private static final long serialVersionUID = 1L;

  public CancelledOrderCannotBeModifiedException(Long orderId) {
    super("Cancelled order [" + orderId + "] can not be modified");
  }
}
