package io.github.codecraft87.eshop.catalog.exceptions;

public class GenericException extends RuntimeException {
  private static final long serialVersionUID = 1L;

  public GenericException(String errMsg) {
    super(errMsg);
  }
}
