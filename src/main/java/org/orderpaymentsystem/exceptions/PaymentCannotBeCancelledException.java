package org.orderpaymentsystem.exceptions;

public class PaymentCannotBeCancelledException extends RuntimeException {

	public PaymentCannotBeCancelledException(Long orderId) {
		super("Payment can not be cancel for ["+orderId+"]");
	}
}
