package io.github.codecraft87.eshop.order.exceptions;

public class OrderAlreadyCancelledException extends RuntimeException {
  private static final long serialVersionUID = 1L;

  public OrderAlreadyCancelledException(Long orderId) {
    super("Order [" + orderId + "] is already cancelled ");
  }
}
