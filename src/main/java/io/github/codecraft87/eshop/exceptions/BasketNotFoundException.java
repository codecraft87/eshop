package io.github.codecraft87.eshop.exceptions;

public class BasketNotFoundException extends RuntimeException {
    
    
    private static final long serialVersionUID = 1L;

    public BasketNotFoundException(Long id) {
        super("Basket not found ["+ id + "]");
    }

}
