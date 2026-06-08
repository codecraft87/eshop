package io.github.codecraft87.eshop.order.service;

import java.time.Instant;
import java.util.List;

import org.springframework.stereotype.Service;

import io.github.codecraft87.eshop.common.enums.OrderLifecycleEvent;
import io.github.codecraft87.eshop.common.enums.OrderStatus;
import io.github.codecraft87.eshop.exceptions.CancelledOrderCannotBeModifiedException;
import io.github.codecraft87.eshop.exceptions.OrderAlreadyCancelledException;
import io.github.codecraft87.eshop.exceptions.OrderCannotBeModifiedException;
import io.github.codecraft87.eshop.exceptions.OrderNotFoundException;
import io.github.codecraft87.eshop.notification.service.NotificationService;
import io.github.codecraft87.eshop.order.dto.OrderRequest;
import io.github.codecraft87.eshop.order.dto.OrderResponse;
import io.github.codecraft87.eshop.order.entity.Order;
import io.github.codecraft87.eshop.order.mapper.OrderMapper;
import io.github.codecraft87.eshop.order.repository.OrderRepository;
import io.github.codecraft87.eshop.payment.service.PaymentService;
import jakarta.transaction.Transactional;

@Service
public class OrderService {

    private final NotificationService notificationService;

    private final OrderRepository repo;


    private final PaymentService paymentService;

    public OrderService(
            OrderRepository repo, 
            PaymentService service, 
            NotificationService notificationService) {
        this.repo = repo;
        this.paymentService = service;
        this.notificationService = notificationService;
    }

    @Transactional
    public Long createOrder(OrderRequest dto) {
        
        final Order order = OrderMapper.getOrderEntity(dto);

        markOrderStatus(order, OrderStatus.CREATED, 
                            OrderLifecycleEvent.CREATE);
        Long orderId = saveOrder(order).getId();
        
        notificationService.orderCreated(orderId);
        
        return orderId;
    }

    @Transactional
    public OrderResponse cancelOrder(Long orderId) {
        final Order orderToCancel = getOrderById(orderId);

        validateIfOrderEligibleForCancellation(orderId, orderToCancel);

        paymentService.handleOrderCancellation(orderId);

        markOrderStatus(orderToCancel, OrderStatus.ORDER_CANCELLED, OrderLifecycleEvent.UPDATE);

        final Order cancelledOrder = saveOrder(orderToCancel);

        notificationService.orderCancelled(orderId);

        return OrderMapper.getOrderResponse(cancelledOrder);
    }

    @Transactional
    public OrderRequest updateOrder(
            Long orderId, 
            OrderRequest orderDto) {
        final Order orderToupdate = getOrderById(orderId);
        validateOrderCanBeModified(orderId, orderToupdate);  
   
        orderToupdate.setTotalAmount(orderDto.getTotalAmount());
        
        final Order updated = saveOrder(orderToupdate);

        notificationService.orderUpdated(updated.getId());
        
        return OrderMapper.getOrderResponse(updated);
    }

    public OrderResponse getOrderDetails(Long orderId) {
        final Order orderDetails = getOrderById(orderId);
        return OrderMapper.getOrderResponse(orderDetails);
    }

    private void validateOrderCanBeModified(Long orderId, Order orderToupdate) {
        if (orderToupdate.getStatus() == OrderStatus.ORDER_CANCELLED) {
            throw new CancelledOrderCannotBeModifiedException(orderId);
        }

        if (orderToupdate.getStatus() == OrderStatus.PAYMENT_DONE
                || orderToupdate.getStatus() == OrderStatus.PAYMENT_FAILED) {
            throw new OrderCannotBeModifiedException(orderId);
        }
    }

    private void validateIfOrderEligibleForCancellation(Long orderId, Order orderToCancel) {
        if (orderToCancel.getStatus() == OrderStatus.ORDER_CANCELLED) {
            throw new OrderAlreadyCancelledException(orderId);
        }
        if (orderToCancel.getStatus() == OrderStatus.PAYMENT_DONE) {
            throw new OrderCannotBeModifiedException(orderId);
        }
    }

    private Order getOrderById(Long orderId) {
        final Order order = repo
                        .findById(orderId)
                        .orElseThrow(
                                () -> new OrderNotFoundException(
                                        orderId));
        return order;
    }

    private Order saveOrder(Order order) {
        order.setUpdatedAt(Instant.now());
        return repo.save(order);
    }

    private void markOrderStatus(Order order, OrderStatus orderStatus, OrderLifecycleEvent orderlifecycleEvent) {
        final Instant now = Instant.now();
        order.setStatus(orderStatus);
        if (orderlifecycleEvent == OrderLifecycleEvent.CREATE) {
            order.setCreatedAt(now);
        }
        order.setUpdatedAt(now);
    }

    public List<OrderResponse> getOrders(Long userId) {
        List<Order> orders = repo
                .findByUserIdAndStatus(
                        userId.toString(), 
                        OrderStatus.CREATED);
        List<OrderResponse> userOrders = orders.stream()
                .map(OrderMapper::getOrderResponse).toList();
        return userOrders;
    }
}