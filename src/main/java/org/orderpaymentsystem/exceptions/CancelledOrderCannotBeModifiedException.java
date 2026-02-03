package org.orderpaymentsystem.exceptions;

public class CancelledOrderCannotBeModifiedException extends RuntimeException {
	public CancelledOrderCannotBeModifiedException(Long orderId) {
        super("Cancelled order [" + orderId + "] can not be modified");
    }
}
