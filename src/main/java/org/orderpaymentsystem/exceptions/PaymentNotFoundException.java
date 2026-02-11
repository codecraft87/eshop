package org.orderpaymentsystem.exceptions;

public class PaymentNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public PaymentNotFoundException(Long id) {
        super("Invalid payment id  [" + id + "]");
    }
}
