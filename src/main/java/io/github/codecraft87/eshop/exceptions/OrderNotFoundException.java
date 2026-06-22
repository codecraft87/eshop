package io.github.codecraft87.eshop.exceptions;

public class OrderNotFoundException extends RuntimeException {
  private static final long serialVersionUID = 1L;

  public OrderNotFoundException(Long id) {
    super("Order not found [" + id + "]");
  }
}
