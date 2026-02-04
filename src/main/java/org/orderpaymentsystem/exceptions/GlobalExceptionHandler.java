package org.orderpaymentsystem.exceptions;

import java.time.Instant;

import org.orderpaymentsystem.common.enums.ErrorEnums;
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
						ErrorEnums.ORDER_NOT_FOUND,
						HttpStatus.NOT_FOUND.value(),
						ex.getMessage(),						
						Instant.now()));
	}
	
	@ExceptionHandler(OrderAlreadyCancelledException.class)
	public ResponseEntity<ErrorResponse> handleAlreadyCancelled(OrderAlreadyCancelledException ex){
		
		return ResponseEntity
				.status(HttpStatus.BAD_REQUEST)
				.body(new ErrorResponse(
						ErrorEnums.ORDER_ALREADY_CANCELLED,
						HttpStatus.BAD_REQUEST.value(),
						ex.getMessage(),
						Instant.now()));
	}
	
	@ExceptionHandler(OrderCannotBeModifiedException.class)
	public ResponseEntity<ErrorResponse> handleOrderCannotBeModified(OrderCannotBeModifiedException ex){
		
		return ResponseEntity
				.status(HttpStatus.BAD_REQUEST)
				.body(new ErrorResponse(
						ErrorEnums.ORDER_CANNOT_BE_MODIFIED,
						HttpStatus.BAD_REQUEST.value(),
						ex.getMessage(),
						Instant.now()));
	}
	
	@ExceptionHandler(PaymentNotFoundException.class)
	public ResponseEntity<ErrorResponse> handlePaymentNotFound(PaymentNotFoundException ex){
		
		return ResponseEntity
				.status(HttpStatus.NOT_FOUND)
				.body(new ErrorResponse(
						ErrorEnums.PAYMENT_NOT_FOUND,
						HttpStatus.NOT_FOUND.value(),
						ex.getMessage(),
						Instant.now()));
	}
	
	@ExceptionHandler(DuplicatePaymentException.class)
	public ResponseEntity<ErrorResponse> handleDuplicatePayment(DuplicatePaymentException ex){
		
		return ResponseEntity
				.status(HttpStatus.CONFLICT)
				.body(new ErrorResponse(
						ErrorEnums.DUPLICATE_PAYMENT,
						HttpStatus.CONFLICT.value(),
						ex.getMessage(),
						Instant.now()));
	}
	
	@ExceptionHandler(InvalidOrderStateForPaymentException.class)
	public ResponseEntity<ErrorResponse> handleInvalidOrderState(InvalidOrderStateForPaymentException ex){
		
		return ResponseEntity
				.status(HttpStatus.BAD_REQUEST)
				.body(new ErrorResponse(
						ErrorEnums.INVALID_ORDER_STATE,
						HttpStatus.BAD_REQUEST.value(),
						ex.getMessage(),
						Instant.now()));
	}
	
	@ExceptionHandler(PaymentCannotBeRetriedException.class)
	public ResponseEntity<ErrorResponse> handlePaymentRetryNotAllowed(PaymentCannotBeRetriedException ex){
		
		return ResponseEntity
				.status(HttpStatus.BAD_REQUEST)
				.body(new ErrorResponse(
						ErrorEnums.PAYMENT_CANNOT_BE_RETRIED,
						HttpStatus.BAD_REQUEST.value(),
						ex.getMessage(),
						Instant.now()));
	}
	
	@ExceptionHandler(CancelledOrderCannotBeModifiedException.class)
	public ResponseEntity<ErrorResponse> handleCancelledOrderCannotBeModified(CancelledOrderCannotBeModifiedException ex){
		
		return ResponseEntity
				.status(HttpStatus.BAD_REQUEST)
				.body(new ErrorResponse(
						ErrorEnums.CANCELLED_ORDER_CANNOT_BE_MODIFIED,
						HttpStatus.BAD_REQUEST.value(),
						ex.getMessage(),
						Instant.now()));
	}
	
	@ExceptionHandler(OrderNotFoundForPaymentException.class)
	public ResponseEntity<ErrorResponse> handleOrderNotFoundForPaymentException(OrderNotFoundForPaymentException ex){
		
		return ResponseEntity
				.status(HttpStatus.BAD_REQUEST)
				.body(new ErrorResponse(
						ErrorEnums.ORDER_NOT_FOUND_FOR_PAYMENT,
						HttpStatus.BAD_REQUEST.value(),
						ex.getMessage(),
						Instant.now()));
	}
	
	@ExceptionHandler(PaymentCannotBeCancelledException.class)
	public ResponseEntity<ErrorResponse> handlePaymentCannotBeCancelled(PaymentCannotBeCancelledException ex){
		
		return ResponseEntity
				.status(HttpStatus.BAD_REQUEST)
				.body(new ErrorResponse(
						ErrorEnums.PAYMENT_CANNOT_BE_CANCELLED,
						HttpStatus.BAD_REQUEST.value(),
						ex.getMessage(),
						Instant.now()));
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleGeneralException(Exception ex){
		
		return ResponseEntity
				.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body(new ErrorResponse(
						ErrorEnums.INTERNAL_SERVER_ERROR,
						HttpStatus.INTERNAL_SERVER_ERROR.value(),
						ex.getMessage(),
						Instant.now()));
	}
}
