package org.orderpaymentsystem.service;

import java.time.Instant;

import org.orderpaymentsystem.common.enums.OrderLifecycleEvent;
import org.orderpaymentsystem.common.enums.OrderStatus;
import org.orderpaymentsystem.dto.OrderDTO;
import org.orderpaymentsystem.entity.Order;
import org.orderpaymentsystem.exceptions.CancelledOrderCannotBeModifiedException;
import org.orderpaymentsystem.exceptions.OrderAlreadyCancelledException;
import org.orderpaymentsystem.exceptions.OrderCannotBeModifiedException;
import org.orderpaymentsystem.exceptions.OrderNotFoundException;
import org.orderpaymentsystem.repository.OrderRepository;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

@Service
public class OrderService {

	private final OrderRepository repo;
	
	private final PaymentService paymentService;
	
	public OrderService(OrderRepository repo, PaymentService service) {
		this.repo = repo;
		this.paymentService = service;
	}
	
	@Transactional
	public Long createOrder(OrderDTO dto) {
		final Order order = Order.getOrderEntity(dto);
		
		markOrderStatus(order, OrderStatus.CREATED, OrderLifecycleEvent.CREATE);
		
		return saveOrder(order).getId();
	}

	@Transactional
	public OrderDTO cancelOrder(Long orderId){
		final Order orderToCancel = getOrderById(orderId);
		
		validateIfOrderEligibleForCancellation(orderId, orderToCancel);
		
		paymentService.handleOrderCancellation(orderId);
		
		markOrderStatus(orderToCancel, OrderStatus.ORDER_CANCELLED, OrderLifecycleEvent.UPDATE);
		
		final Order cancelledOrder = saveOrder(orderToCancel);
		
		return OrderDTO.getOrderDTO(cancelledOrder);
	}

	@Transactional
	public OrderDTO updateOrder(OrderDTO orderDto) {
		final Order orderToupdate= getOrderById(orderDto.getOrderId());
		
		validateOrderCanBeModified(orderDto, orderToupdate);
		
		orderToupdate.setUpdatedAt(Instant.now());
		orderToupdate.setAmount(orderDto.getAmount());
		orderToupdate.setUserId(orderDto.getUserId());
		
		final Order updated = saveOrder(orderToupdate);
		
		return OrderDTO.getOrderDTO(updated);
	}
	

	public OrderDTO getOrderDetails(Long orderId) {
		final Order orderDetails = getOrderById(orderId);
		return OrderDTO.getOrderDTO(orderDetails);
	}
	

	private void validateOrderCanBeModified(OrderDTO orderDto, Order orderToupdate) {
		if(orderToupdate.getStatus() == OrderStatus.ORDER_CANCELLED) {
			throw new CancelledOrderCannotBeModifiedException(orderDto.getOrderId());
		}
		
		if(orderToupdate.getStatus() == OrderStatus.PAYMENT_DONE || 
			orderToupdate.getStatus() == OrderStatus.PAYMENT_FAILED) {
			 	throw new OrderCannotBeModifiedException(orderDto.getOrderId());
			}
	}

	private void validateIfOrderEligibleForCancellation(Long orderId, Order orderToCancel) {
		if(orderToCancel.getStatus() == OrderStatus.ORDER_CANCELLED) {
			 throw new OrderAlreadyCancelledException(orderId);
		}
		if (orderToCancel.getStatus() == OrderStatus.PAYMENT_DONE) {
		    throw new OrderCannotBeModifiedException(orderId);
		}
	}

	private Order getOrderById(Long orderId) {
		final Order order = repo.findById(orderId)
				.orElseThrow(() -> new OrderNotFoundException(orderId));
		return order;
	}

	private Order saveOrder(Order order) {
		return repo.save(order);
	}

	private void markOrderStatus(Order order, OrderStatus orderStatus, OrderLifecycleEvent orderlifecycleEvent) {
		final Instant now = Instant.now();
		order.setStatus(orderStatus);
		if(orderlifecycleEvent==OrderLifecycleEvent.CREATE) {
			order.setCreatedAt(now);
		}
		order.setUpdatedAt(now);
	}
}