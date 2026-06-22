package io.github.codecraft87.eshop.exceptions;

public class ProductNotFoundException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public ProductNotFoundException(Long id) {
    super("Product not found [" + id + "]");
  }
}
