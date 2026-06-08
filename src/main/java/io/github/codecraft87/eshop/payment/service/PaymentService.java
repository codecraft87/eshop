package io.github.codecraft87.eshop.payment.service;

import java.time.Instant;
import java.util.List;

import org.springframework.stereotype.Service;

import io.github.codecraft87.eshop.common.enums.OrderLifecycleEvent;
import io.github.codecraft87.eshop.common.enums.OrderStatus;
import io.github.codecraft87.eshop.common.enums.PaymentStatus;
import io.github.codecraft87.eshop.exceptions.DuplicatePaymentException;
import io.github.codecraft87.eshop.exceptions.InvalidOrderStateForPaymentException;
import io.github.codecraft87.eshop.exceptions.PaymentCannotBeCancelledException;
import io.github.codecraft87.eshop.exceptions.PaymentCannotBeRetriedException;
import io.github.codecraft87.eshop.exceptions.PaymentNotFoundException;
import io.github.codecraft87.eshop.notification.service.NotificationModuleService;
import io.github.codecraft87.eshop.order.entity.Order;
import io.github.codecraft87.eshop.order.service.OrderModuleService;
import io.github.codecraft87.eshop.payment.dto.PaymentRequest;
import io.github.codecraft87.eshop.payment.dto.PaymentResponse;
import io.github.codecraft87.eshop.payment.entity.Payment;
import io.github.codecraft87.eshop.payment.mapper.PaymentMapper;
import io.github.codecraft87.eshop.payment.repository.PaymentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PaymentService implements PaymentModuleService {

    private final NotificationModuleService notificationService;

    private final PaymentRepository paymentRepo;

    private final OrderModuleService orderService;
    

    @Transactional
    public Long processPayment(PaymentRequest paymentRequest) {

        validatePaymentRequest(paymentRequest.getOrderId());

        final Payment paymentToBeProcesed = PaymentMapper.getPaymentEntity(paymentRequest);

        if (paymentRequest.isSimulateFailure()) {

            markPaymentStatus(paymentToBeProcesed, PaymentStatus.PAYMENT_FAILED, OrderLifecycleEvent.CREATE);

            Long paymentId = completePayment(paymentToBeProcesed, PaymentStatus.PAYMENT_FAILED, OrderStatus.PAYMENT_FAILED);
            
            notificationService.paymentFailed(paymentId);

            return paymentId;
        } else {

            checkForDuplicatePayment(paymentRequest.getOrderId());

            markPaymentStatus(paymentToBeProcesed, PaymentStatus.PAYMENT_DONE, OrderLifecycleEvent.CREATE);

            Long paymentId = completePayment(paymentToBeProcesed, PaymentStatus.PAYMENT_DONE, OrderStatus.PAYMENT_DONE);

            notificationService.paymentSucceeded(paymentId);

            return paymentId;
        }
    }

    @Transactional
    public Long retryPayment(long paymentId) {

        final Payment paymentToBeRetry = getPaymentById(paymentId);

        validateRePaymentRequest(paymentToBeRetry.getOrderId());

        checkForDuplicatePayment(paymentToBeRetry.getOrderId());

        markPaymentStatus(paymentToBeRetry, PaymentStatus.PAYMENT_DONE, OrderLifecycleEvent.UPDATE);
        completePayment(paymentToBeRetry, PaymentStatus.PAYMENT_DONE, OrderStatus.PAYMENT_DONE);

        notificationService.paymentSucceeded(paymentId);

        return paymentId;
    }

    @Transactional
    public void handleOrderCancellation(Long orderId) {
        final List<Payment> payments = getPaymentListForOrder(orderId);

        final boolean hasInvalidStatus = payments.stream().anyMatch(
                p -> p.getStatus() == PaymentStatus.PAYMENT_DONE 
                || p.getStatus() == PaymentStatus.PAYMENT_CANCELLED);

        if (hasInvalidStatus) {
            throw new PaymentCannotBeCancelledException(orderId);
        }

        payments.stream()
                .forEach(
                        p -> p.setStatus(PaymentStatus.PAYMENT_CANCELLED));
        paymentRepo.saveAll(payments);
        notificationService.paymentCancelled(orderId);

    }

    public PaymentResponse getPaymentDetails(Long paymentId) {
        final Payment payment = 
                paymentRepo.findById(paymentId)
                .orElseThrow(() -> new PaymentNotFoundException(paymentId));
        return PaymentMapper.getPaymentResponse(payment);
    }

    private List<Payment> getPaymentListForOrder(Long orderId) {
        final List<Payment> payments = paymentRepo.findByOrderId(orderId);
        return payments;
    }

    private Payment getPaymentById(long paymentId) {
        final Payment paymentToBeRetry = 
                paymentRepo.findById(paymentId)
                .orElseThrow(() -> 
                    new PaymentNotFoundException(paymentId));
        return paymentToBeRetry;
    }

    private void checkForDuplicatePayment(Long orderId) {
        if (paymentRepo.existsByOrderIdAndStatus(orderId, PaymentStatus.PAYMENT_DONE)) {
            throw new DuplicatePaymentException(orderId);
        }
    }

    private Long completePayment(Payment paymentToBeProcesed, PaymentStatus paymentStatus, OrderStatus orderStatus) {

        final Payment payment = savePayment(paymentToBeProcesed);

        updateOrderStatus(payment.getOrderId(), orderStatus);
        return payment.getId();
    }
    
    private Payment savePayment(Payment payment) {
        payment.setUpdatedAt(Instant.now());
        Payment savedPayment = paymentRepo.save(payment);
        return savedPayment;
        
    }

    private void markPaymentStatus(Payment paymentToBeProcesed, PaymentStatus paymentStatus,
        OrderLifecycleEvent isCreateOperation) {
        final Instant now = Instant.now();
        paymentToBeProcesed.setStatus(paymentStatus);
        if (isCreateOperation == OrderLifecycleEvent.CREATE) {
            paymentToBeProcesed.setCreatedAt(now);
        }
        paymentToBeProcesed.setUpdatedAt(now);
    }

    private void validatePaymentRequest(Long orderId) {
        final Order order = orderService.getOrder(orderId);
        if (order.getStatus() != OrderStatus.CREATED) {
            throw new InvalidOrderStateForPaymentException(order.getId());
        }
    }

    private void validateRePaymentRequest(Long orderId) {
        final Order order = orderService.getOrder(orderId);
        if (order.getStatus() != OrderStatus.PAYMENT_FAILED) {
            throw new PaymentCannotBeRetriedException(orderId);
        }
    }

    private void updateOrderStatus(Long orderId, OrderStatus status) {
        final Order order = orderService.getOrder(orderId);
        order.setStatus(status);
        orderService.saveOrder(order);
    }
}
