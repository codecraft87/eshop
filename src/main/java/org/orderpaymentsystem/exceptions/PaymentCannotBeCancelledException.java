package org.orderpaymentsystem.exceptions;

public class PaymentCannotBeCancelledException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public PaymentCannotBeCancelledException(Long orderId) {
        super("Payment can not be cancel for [" + orderId + "]");
    }
}
