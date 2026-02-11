package org.orderpaymentsystem.exceptions;

public class InvalidOrderStateForPaymentException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public InvalidOrderStateForPaymentException(Long id) {
        super("Payment is not allowed for this order [" + id + "]");
    }
}
