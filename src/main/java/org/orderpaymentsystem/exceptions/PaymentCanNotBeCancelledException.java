package org.orderpaymentsystem.exceptions;

public class PaymentCanNotBeCancelledException extends RuntimeException {

	public PaymentCanNotBeCancelledException(Long orderId) {
		super("Payment can not be cancel for ["+orderId+"]");
	}
}
