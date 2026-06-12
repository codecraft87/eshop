package io.github.codecraft87.eshop.payment.service;

import java.time.Instant;

import org.springframework.stereotype.Service;

import io.github.codecraft87.eshop.exceptions.DuplicatePaymentException;
import io.github.codecraft87.eshop.exceptions.PaymentNotFoundException;
import io.github.codecraft87.eshop.messaging.event.PaymentCompleteEvent;
import io.github.codecraft87.eshop.messaging.event.PaymentFailedEvent;
import io.github.codecraft87.eshop.notification.service.NotificationModuleService;
import io.github.codecraft87.eshop.order.enums.OrderLifecycleEvent;
import io.github.codecraft87.eshop.payment.dto.PaymentRequest;
import io.github.codecraft87.eshop.payment.dto.PaymentResponse;
import io.github.codecraft87.eshop.payment.entity.Payment;
import io.github.codecraft87.eshop.payment.enums.PaymentStatus;
import io.github.codecraft87.eshop.payment.mapper.PaymentMapper;
import io.github.codecraft87.eshop.payment.publisher.PaymentCompletedEventPublisher;
import io.github.codecraft87.eshop.payment.publisher.PaymentFailedEventPublisher;
import io.github.codecraft87.eshop.payment.repository.PaymentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Service
@Slf4j
public class PaymentService implements PaymentModuleService {

    private final NotificationModuleService notificationService;

    private final PaymentRepository paymentRepo;
   
    private final PaymentCompletedEventPublisher paymentCompletePublisher;
    
    private final PaymentFailedEventPublisher paymentFailedEventPublisher;
    
    @Transactional
    public Long processPayment(PaymentRequest paymentRequest) {
        log.info("Processing payment for order {}", paymentRequest);
//        validatePaymentRequest(paymentRequest.getOrderId());

        final Payment paymentToBeProcesed = PaymentMapper.getPaymentEntity(paymentRequest);

        if (paymentRequest.isSimulateFailure()) {
            log.info("Payment failure case");
            markPaymentStatus(paymentToBeProcesed, PaymentStatus.PAYMENT_FAILED, OrderLifecycleEvent.PENDING_PAYMENT);

            Long paymentId = completePayment(paymentToBeProcesed, PaymentStatus.PAYMENT_FAILED);
            
            paymentFailedEventPublisher.publishPaymentFailedEvent(
                        new PaymentFailedEvent(
                                paymentRequest.getOrderId()));
            notificationService.paymentFailed(paymentId);

            return paymentId;
        } else {
            log.info("else block");
            checkForDuplicatePayment(paymentRequest.getOrderId());

            markPaymentStatus(paymentToBeProcesed, PaymentStatus.PAYMENT_DONE, OrderLifecycleEvent.PENDING_PAYMENT);

            Long paymentId = completePayment(paymentToBeProcesed, PaymentStatus.PAYMENT_DONE);

            paymentCompletePublisher.publishPaymentRequestedEvent(
                    new PaymentCompleteEvent(paymentRequest.getOrderId()));
            notificationService.paymentSucceeded(paymentId);

            return paymentId;
        }
    }

    @Transactional
    public Long retryPayment(long paymentId) {
        log.info("Retry payment {} ",paymentId);
        final Payment paymentToBeRetry = getPaymentById(paymentId);

//        validateRePaymentRequest(paymentToBeRetry.getOrderId());

        checkForDuplicatePayment(paymentToBeRetry.getOrderId());

        markPaymentStatus(paymentToBeRetry, PaymentStatus.PAYMENT_DONE, OrderLifecycleEvent.UPDATE);
        completePayment(paymentToBeRetry, PaymentStatus.PAYMENT_DONE);
        paymentCompletePublisher.publishPaymentRequestedEvent(
                new PaymentCompleteEvent(paymentToBeRetry.getOrderId()));
        notificationService.paymentSucceeded(paymentId);

        return paymentId;
    }

    public PaymentResponse getPaymentDetails(Long paymentId) {
        final Payment payment = 
                paymentRepo.findById(paymentId)
                .orElseThrow(() -> new PaymentNotFoundException(paymentId));
        return PaymentMapper.getPaymentResponse(payment);
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

    private Long completePayment(Payment paymentToBeProcesed, 
                                 PaymentStatus paymentStatus) {

        final Payment payment = savePayment(paymentToBeProcesed);

       // updateOrderStatus(payment.getOrderId(), orderStatus);
        return payment.getId();
    }
    
    private Payment savePayment(Payment payment) {
        payment.setUpdatedAt(Instant.now());
        Payment savedPayment = paymentRepo.save(payment);
        return savedPayment;
        
    }

    private void markPaymentStatus(Payment paymentToBeProcesed, 
                                        PaymentStatus paymentStatus,
                                        OrderLifecycleEvent isCreateOperation) {
        final Instant now = Instant.now();
        paymentToBeProcesed.setStatus(paymentStatus);
        if (isCreateOperation == OrderLifecycleEvent.PENDING_PAYMENT) {
            paymentToBeProcesed.setCreatedAt(now);
        }
        paymentToBeProcesed.setUpdatedAt(now);
    }

//    private void validatePaymentRequest(Long orderId) {
//        final Order order = orderService.getOrder(orderId);
//        log.info("order status {} ",order.getStatus());
//        if (order.getStatus() != OrderStatus.PAYMENT_PENDING) {
//            throw new InvalidOrderStateForPaymentException(order.getId());
//        }
//    }
//
//    private void validateRePaymentRequest(Long orderId) {
//        final Order order = orderService.getOrder(orderId);
//        if (order.getStatus() != OrderStatus.PAYMENT_FAILED) {
//            throw new PaymentCannotBeRetriedException(orderId);
//        }
//    }

    /*
     * private void updateOrderStatus(Long orderId, OrderStatus status) { final
     * Order order = orderService.getOrder(orderId); order.setStatus(status);
     * orderService.saveOrder(order); }
     */
}
