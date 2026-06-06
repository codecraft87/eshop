package io.github.codecraft87.eshop.exceptions;

public class InvalidOrderStateForPaymentException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public InvalidOrderStateForPaymentException(Long id) {
        super("Payment is not allowed for this order [" + id + "]");
    }
}
