package io.github.codecraft87.eshop.order.messaging.consumer;

import java.util.UUID;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import io.github.codecraft87.eshop.order.messaging.config.QueueConstants;
import io.github.codecraft87.eshop.order.messaging.event.PaymentAckowledge;
import io.github.codecraft87.eshop.order.messaging.idempotency.OrderProcessedEventService;
import io.github.codecraft87.eshop.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import tools.jackson.databind.ObjectMapper;

@RequiredArgsConstructor
@Service
@Slf4j
public class OrderPaymentCompletedConsumer {

  private final OrderService orderService;

  private final ObjectMapper objectMapper;

  private final OrderProcessedEventService processedEventService;

  @RabbitListener(queues = QueueConstants.ORDER_PAYMENT_COMPLETED_QUEUE)
  public void handlePaymentRequestedEvent(String payload) {
    log.info("Received Payment requested event ");

    PaymentAckowledge paymentCompleted = objectMapper.readValue(payload, PaymentAckowledge.class);
    if (paymentCompleted != null) {
      UUID eventId = UUID.fromString(paymentCompleted.eventId());
      if (processedEventService.checkIfEventIsProcessed(eventId)) {
        log.info("Duplicate event {} ignored", eventId);
        return;
      }
      orderService.updateOrderForPayment(paymentCompleted.orderId());
      processedEventService.addProcessedEventEntry(eventId);
    }
  }
}
