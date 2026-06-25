package io.github.codecraft87.eshop.exceptions;

public class ResourceNotFoundException extends RuntimeException {
  private static final long serialVersionUID = 1L;

  public ResourceNotFoundException(String errMsg) {
    super(errMsg);
  }
}
