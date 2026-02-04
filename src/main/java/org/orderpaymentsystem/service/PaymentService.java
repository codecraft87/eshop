package org.orderpaymentsystem.service;

import java.time.Instant;
import java.util.List;

import org.orderpaymentsystem.common.enums.OrderLifecycleEvent;
import org.orderpaymentsystem.common.enums.OrderStatus;
import org.orderpaymentsystem.common.enums.PaymentStatus;
import org.orderpaymentsystem.dto.PaymentDTO;
import org.orderpaymentsystem.entity.Order;
import org.orderpaymentsystem.entity.Payment;
import org.orderpaymentsystem.exceptions.DuplicatePaymentException;
import org.orderpaymentsystem.exceptions.InvalidOrderStateForPaymentException;
import org.orderpaymentsystem.exceptions.OrderNotFoundException;
import org.orderpaymentsystem.exceptions.OrderNotFoundForPaymentException;
import org.orderpaymentsystem.exceptions.PaymentCannotBeCancelledException;
import org.orderpaymentsystem.exceptions.PaymentCannotBeRetriedException;
import org.orderpaymentsystem.exceptions.PaymentNotFoundException;
import org.orderpaymentsystem.repository.OrderRepository;
import org.orderpaymentsystem.repository.PaymentRepository;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Service
public class PaymentService {

	private final PaymentRepository paymentRepo;
	
	private final OrderRepository orderRepo;
	
	public PaymentService(PaymentRepository repository, OrderRepository orderRepo) {
		this.paymentRepo = repository;
		this.orderRepo = orderRepo;
	}
	
	@Transactional
	public Long processPayment(PaymentDTO paymentDTO) {
		
		validatePaymentRequest(paymentDTO.getOrderId());
		
		final Payment paymentToBeProcesed = Payment.getPaymentEntity(paymentDTO);
		
		if(paymentDTO.isSimulateFailure()) {
			
			markPaymentStatus(paymentToBeProcesed, PaymentStatus.PAYMENT_FAILED, OrderLifecycleEvent.CREATE);
			
			return completePayment(paymentToBeProcesed, PaymentStatus.PAYMENT_FAILED, OrderStatus.PAYMENT_FAILED);
		}else {
			
			checkForDuplicatePayment(paymentDTO.getOrderId());
			
			markPaymentStatus(paymentToBeProcesed, PaymentStatus.PAYMENT_DONE, OrderLifecycleEvent.CREATE);
			
			return completePayment(paymentToBeProcesed, PaymentStatus.PAYMENT_DONE, OrderStatus.PAYMENT_DONE);
		}
	}

	@Transactional
	public Long retryPayment(long paymentId) {
		
		final Payment paymentToBeRetry = getPaymentById(paymentId);
		
		validateRePaymentRequest(paymentToBeRetry.getOrderId());
		
		checkForDuplicatePayment(paymentToBeRetry.getOrderId());	
		
		markPaymentStatus(paymentToBeRetry, PaymentStatus.PAYMENT_DONE, OrderLifecycleEvent.UPDATE);
		
		return completePayment(paymentToBeRetry, PaymentStatus.PAYMENT_DONE, OrderStatus.PAYMENT_DONE);
	}

	@Transactional
	public void handleOrderCancellation(Long orderId) {
		final List<Payment> payments = getPaymentListForOrder(orderId);
		
		final boolean hasInvalidStatus = payments.stream()
				.anyMatch(p->
					p.getStatus()==PaymentStatus.PAYMENT_DONE ||
					p.getStatus()==PaymentStatus.PAYMENT_CANCELLED
						);
		
		if(hasInvalidStatus) {
			throw new PaymentCannotBeCancelledException(orderId);
		}
		
		payments.stream().forEach(p->p.setStatus(PaymentStatus.PAYMENT_CANCELLED));
		paymentRepo.saveAll(payments);
		
	}

	private List<Payment> getPaymentListForOrder(Long orderId) {
		final List<Payment> payments = paymentRepo.findByOrderId(orderId);
		return payments;
	}
	
	
	public PaymentDTO getPaymentDetails(Long paymentId) {
		final Payment payment = paymentRepo.findById(paymentId)
				.orElseThrow(()-> new PaymentNotFoundException(paymentId));
		return PaymentDTO.getPaymentDTO(payment);
	}
	
	private Payment getPaymentById(long paymentId) {
		final Payment paymentToBeRetry = paymentRepo.findById(paymentId)
				.orElseThrow(()-> new PaymentNotFoundException(paymentId));
		return paymentToBeRetry;
	}
	
	private void checkForDuplicatePayment(Long orderId) {
		if(paymentRepo.existsByOrderIdAndStatus(orderId, PaymentStatus.PAYMENT_DONE)) {
			throw new DuplicatePaymentException(orderId);
		}
	}
	
	private Long completePayment(
			Payment paymentToBeProcesed,
			PaymentStatus paymentStatus,
			OrderStatus orderStatus) {
		
		final Long paymentId =  paymentRepo.save(paymentToBeProcesed).getId();
		
		updateOrderStatus(paymentToBeProcesed.getOrderId(), orderStatus);
		return paymentId;
	}

	private void markPaymentStatus(Payment paymentToBeProcesed, PaymentStatus paymentStatus, OrderLifecycleEvent isCreateOperation ) {
		final Instant now = Instant.now();
		paymentToBeProcesed.setStatus(paymentStatus);
		if(isCreateOperation==OrderLifecycleEvent.CREATE) {
			paymentToBeProcesed.setCreatedAt(now);
		}
		paymentToBeProcesed.setUpdatedAt(now);
	}

	private void validatePaymentRequest(Long orderId) {
		final Order order = getOrderById(orderId);
		if(order.getStatus()!=OrderStatus.CREATED) {
			throw new InvalidOrderStateForPaymentException(order.getId());
		}
	}

	private Order getOrderById(Long orderId) {
		final Order order = orderRepo.findById(orderId)
				.orElseThrow(()-> new OrderNotFoundForPaymentException(orderId));
		return order;
	}

	private void validateRePaymentRequest(Long orderId) {
		final Order order = getOrderById(orderId);
		if(order.getStatus()!=OrderStatus.PAYMENT_FAILED) {
			throw new PaymentCannotBeRetriedException(orderId);
		}
	}
	
	private void updateOrderStatus(Long orderId, OrderStatus status) {
		final Order order = orderRepo.findById(orderId)
				.orElseThrow(()->new OrderNotFoundException(orderId));
		order.setStatus(status);
		order.setUpdatedAt(Instant.now());
		orderRepo.save(order);
	}
}
