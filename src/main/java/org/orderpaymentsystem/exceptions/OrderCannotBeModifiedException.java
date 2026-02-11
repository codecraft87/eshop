package org.orderpaymentsystem.exceptions;

public class OrderCannotBeModifiedException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public OrderCannotBeModifiedException(Long orderId) {
        super("Once payment initiated, Order [" + orderId + "] can not be modified");
    }
}
