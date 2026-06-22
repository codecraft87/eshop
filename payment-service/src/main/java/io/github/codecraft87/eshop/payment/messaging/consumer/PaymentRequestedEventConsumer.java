package io.github.codecraft87.eshop.payment.messaging.consumer;

import java.util.UUID;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import io.github.codecraft87.eshop.payment.dto.PaymentRequest;
import io.github.codecraft87.eshop.payment.messaging.config.QueueConstants;
import io.github.codecraft87.eshop.payment.messaging.event.PaymentRequested;
import io.github.codecraft87.eshop.payment.messaging.idempotency.PaymentProcessedEventService;
import io.github.codecraft87.eshop.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import tools.jackson.databind.ObjectMapper;

@RequiredArgsConstructor
@Slf4j
@Service
public class PaymentRequestedEventConsumer {

  private final PaymentService paymentService;

  private final ObjectMapper objectMapper;

  private final PaymentProcessedEventService paymentProcessedEventService;
  
  @RabbitListener(queues = QueueConstants.PAYMENT_ORDER_PAYMENT_REQUESTED_QUEUE)
  public void handlePaymentRequested(String payload) {
    log.info("Received Order payment requested ");
    PaymentRequested paymentRequested = objectMapper.readValue(payload, PaymentRequested.class);
    log.info("payload {} " + paymentRequested);

    if (paymentRequested != null) {
      UUID eventId = UUID.fromString(paymentRequested.eventId());
      if(paymentProcessedEventService.checkIfEventIsProcessed(eventId)) {
          log.info("Duplicate event {} ignored ", eventId);
          return;
      }
      PaymentRequest request = new PaymentRequest();
      request.setOrderId(paymentRequested.orderId());
      request.setPaymentMode(paymentRequested.paymentMode());
      paymentService.processPayment(request);
      paymentProcessedEventService.addProcessedEventEntry(eventId);
    }
  }
}
