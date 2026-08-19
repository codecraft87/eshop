package io.github.codecraft87.eshop.order.messaging.consumer;

import java.util.UUID;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import io.github.codecraft87.eshop.messagingcommon.idempotency.EventIdempotency;
import io.github.codecraft87.eshop.order.messaging.config.OrderQueueConstants;
import io.github.codecraft87.eshop.order.messaging.event.PaymentAckowledge;
import io.github.codecraft87.eshop.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import tools.jackson.databind.ObjectMapper;

@RequiredArgsConstructor
@Slf4j
@Service
public class PaymentFailedConsumer {

  private final OrderService orderService;

  private final ObjectMapper objectMapper;

  private final EventIdempotency orderProcessedEventService;

  @RabbitListener(queues = OrderQueueConstants.ORDER_PAYMENT_FAILED_QUEUE)
  public void handlePaymentFailedEvent(String payload) {
    log.info("Received payment failed event ");
    PaymentAckowledge failedEvent = objectMapper.readValue(payload, PaymentAckowledge.class);
    if (failedEvent != null) {
      UUID eventId = UUID.fromString(failedEvent.eventId());
      if (orderProcessedEventService.isEventProcessed(eventId)) {
        log.info("Duplicate event {} ignored", eventId);
        return;
      }
      orderService.markOrderAsFailed(failedEvent.orderId());
      orderProcessedEventService.saveProcessedEvent(eventId);
    }
  }
}
