package io.github.codecraft87.eshop.basket.exceptions;

public class CatalogServiceUnavailableException extends RuntimeException {
    
    private static final long serialVersionUID = 1L;

    public CatalogServiceUnavailableException(Long id) {
      super("Catalog service is currently unavailable");
    }
}
