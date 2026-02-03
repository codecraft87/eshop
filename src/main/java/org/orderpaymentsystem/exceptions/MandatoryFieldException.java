package org.orderpaymentsystem.exceptions;

public class MandatoryFieldException extends RuntimeException {

	public MandatoryFieldException(String fieldName) {
		super("["+fieldName +"] is mandatory.");
	}

}
