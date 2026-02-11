package org.orderpaymentsystem.exceptions;

public class OrderAlreadyCancelledException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public OrderAlreadyCancelledException(Long orderId) {
        super("Order [" + orderId + "] is already cancelled ");
    }
}
