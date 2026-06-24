package io.github.codecraft87.eshop.order.exceptions;

public class OrderNotFoundForPaymentException extends RuntimeException {
  private static final long serialVersionUID = 1L;

  public OrderNotFoundForPaymentException(Long id) {
    super("Payment cannot be processed because the order does not exist [" + id + "]");
  }
}
