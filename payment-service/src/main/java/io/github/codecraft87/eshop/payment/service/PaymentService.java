package io.github.codecraft87.eshop.payment.service;

import java.time.Instant;

import org.springframework.stereotype.Service;

import io.github.codecraft87.eshop.payment.dto.PaymentRequest;
import io.github.codecraft87.eshop.payment.dto.PaymentResponse;
import io.github.codecraft87.eshop.payment.entity.Payment;
import io.github.codecraft87.eshop.payment.enums.PaymentMode;
import io.github.codecraft87.eshop.payment.enums.PaymentStatus;
import io.github.codecraft87.eshop.payment.exceptions.DuplicatePaymentException;
import io.github.codecraft87.eshop.payment.exceptions.PaymentNotFoundException;
import io.github.codecraft87.eshop.payment.mapper.PaymentMapper;
import io.github.codecraft87.eshop.payment.messaging.outbox.PaymentOutboxService;
import io.github.codecraft87.eshop.payment.repository.PaymentRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Service
@Slf4j
public class PaymentService implements PaymentModuleService {

  private final PaymentRepository paymentRepo;

  private final PaymentOutboxService outboxService;

  @Transactional
  public Long processPayment(PaymentRequest paymentRequest) {
    log.info("Processing payment for order {}", paymentRequest.getOrderId());

    final Payment paymentToBeProcesed = PaymentMapper.getPaymentEntity(paymentRequest);

    if (paymentRequest.getPaymentMode()==PaymentMode.SIMULATED_FAILURE) {
      log.info("Processing failed payment ");

      Payment processedPayment = completePayment(paymentToBeProcesed, PaymentStatus.PAYMENT_FAILED);

      outboxService.savePaymentFailededEvent(paymentRequest.getOrderId());

      return processedPayment.getId();

    } else {
      log.info("Processing success payment ");
      checkForDuplicatePayment(paymentRequest.getOrderId());

      Payment processedPayment = completePayment(paymentToBeProcesed, PaymentStatus.PAYMENT_DONE);

      outboxService.savePaymentCompletedEvent(paymentRequest.getOrderId());

      return processedPayment.getId();
    }
  }

  @Transactional
  public Long retryPayment(long paymentId) {

    log.info("Processing retry payment ");

    final Payment paymentToBeRetry = getPaymentById(paymentId);

    checkForDuplicatePayment(paymentToBeRetry.getOrderId());

    Payment processedPayment = completePayment(paymentToBeRetry, PaymentStatus.PAYMENT_DONE);

    outboxService.savePaymentCompletedEvent(paymentToBeRetry.getOrderId());

    return processedPayment.getId();
  }

  private Payment getPaymentById(long paymentId) {
    final Payment payment =
        paymentRepo.findById(paymentId).orElseThrow(() -> new PaymentNotFoundException(paymentId));

    return payment;
  }

  public PaymentResponse getPaymentDetails(Long paymentId) {
    final Payment payment = getPaymentById(paymentId);

    return PaymentMapper.getPaymentResponse(payment);
  }

  private void checkForDuplicatePayment(Long orderId) {
    log.info("checking duplicate payment for order id {}", orderId);
    if (paymentRepo.existsByOrderIdAndStatus(orderId, PaymentStatus.PAYMENT_DONE)) {
      throw new DuplicatePaymentException(orderId);
    }
  }

  private Payment completePayment(Payment paymentToBeProcesed, PaymentStatus paymentStatus) {
    paymentToBeProcesed.setStatus(paymentStatus);
    final Instant now = Instant.now();
    paymentToBeProcesed.setCreatedAt(now);
    paymentToBeProcesed.setUpdatedAt(now);
    Payment savedPayment = paymentRepo.save(paymentToBeProcesed);
    return savedPayment;
  }
}
