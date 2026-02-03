package org.orderpaymentsystem.exceptions;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

	
	@ExceptionHandler(OrderNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleOrderNotFound(OrderNotFoundException ex){
		
		return ResponseEntity
				.status(HttpStatus.NOT_FOUND)
				.body(new ErrorResponse(
						"ORDER_NOT_FOUND",
						HttpStatus.NOT_FOUND.value(),
						ex.getMessage(),						
						LocalDateTime.now()));
	}
	
	@ExceptionHandler(OrderAlreadyCancelledException.class)
	public ResponseEntity<ErrorResponse> handleAlreadyCancelled(OrderAlreadyCancelledException ex){
		
		return ResponseEntity
				.status(HttpStatus.BAD_REQUEST)
				.body(new ErrorResponse(
						"ORDER_ALREADY_CANCELLED",
						HttpStatus.BAD_REQUEST.value(),
						ex.getMessage(),
						LocalDateTime.now()));
	}
	
	@ExceptionHandler(OrderCannotBeModifiedException.class)
	public ResponseEntity<ErrorResponse> handleOrderCannotBeModified(OrderCannotBeModifiedException ex){
		
		return ResponseEntity
				.status(HttpStatus.BAD_REQUEST)
				.body(new ErrorResponse(
						"ORDER_CANNOT_BE_MODIFIED",
						HttpStatus.BAD_REQUEST.value(),
						ex.getMessage(),
						LocalDateTime.now()));
	}
	
	@ExceptionHandler(PaymentNotFoundException.class)
	public ResponseEntity<ErrorResponse> handlePaymentNotFound(PaymentNotFoundException ex){
		
		return ResponseEntity
				.status(HttpStatus.NOT_FOUND)
				.body(new ErrorResponse(
						"PAYMENT_NOT_FOUND",
						HttpStatus.NOT_FOUND.value(),
						ex.getMessage(),
						LocalDateTime.now()));
	}
	
	@ExceptionHandler(DuplicatePaymentException.class)
	public ResponseEntity<ErrorResponse> handleDuplicatePayment(DuplicatePaymentException ex){
		
		return ResponseEntity
				.status(HttpStatus.CONFLICT)
				.body(new ErrorResponse(
						"DUPLICATE_PAYMENT",
						HttpStatus.CONFLICT.value(),
						ex.getMessage(),
						LocalDateTime.now()));
	}
	
	@ExceptionHandler(InvalidOrderStateForPaymentException.class)
	public ResponseEntity<ErrorResponse> handleInvalidOrderState(InvalidOrderStateForPaymentException ex){
		
		return ResponseEntity
				.status(HttpStatus.BAD_REQUEST)
				.body(new ErrorResponse(
						"INVALID_ORDER_STATE",
						HttpStatus.BAD_REQUEST.value(),
						ex.getMessage(),
						LocalDateTime.now()));
	}
	
	@ExceptionHandler(PaymentCannotBeRetriedException.class)
	public ResponseEntity<ErrorResponse> handlePaymentRetryNotAllowed(PaymentCannotBeRetriedException ex){
		
		return ResponseEntity
				.status(HttpStatus.BAD_REQUEST)
				.body(new ErrorResponse(
						"PAYMENT_CANNOT_BE_RETRIED",
						HttpStatus.BAD_REQUEST.value(),
						ex.getMessage(),
						LocalDateTime.now()));
	}
	
	@ExceptionHandler(CancelledOrderCannotBeModifiedException.class)
	public ResponseEntity<ErrorResponse> handleCancelledOrderCannotBeModified(CancelledOrderCannotBeModifiedException ex){
		
		return ResponseEntity
				.status(HttpStatus.BAD_REQUEST)
				.body(new ErrorResponse(
						"CANCELLED_ORDER_CANNOT_BE_MODIFIED",
						HttpStatus.BAD_REQUEST.value(),
						ex.getMessage(),
						LocalDateTime.now()));
	}
	
	@ExceptionHandler(MandatoryFieldException.class)
	public ResponseEntity<ErrorResponse> handleMandatoryFields(MandatoryFieldException ex){
		
		return ResponseEntity
				.status(HttpStatus.BAD_REQUEST)
				.body(new ErrorResponse(
						"MANDATORY_FIELD",
						HttpStatus.BAD_REQUEST.value(),
						ex.getMessage(),
						LocalDateTime.now()));
	}
	
	@ExceptionHandler(OrderNotFoundForPaymentException.class)
	public ResponseEntity<ErrorResponse> handleOrderNotFoundForPaymentException(OrderNotFoundForPaymentException ex){
		
		return ResponseEntity
				.status(HttpStatus.BAD_REQUEST)
				.body(new ErrorResponse(
						"ORDER_NOT_FOUND_FOR_PAYMENTS",
						HttpStatus.BAD_REQUEST.value(),
						ex.getMessage(),
						LocalDateTime.now()));
	}
}
