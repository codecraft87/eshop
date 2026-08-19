package io.github.codecraft87.eshop.order.messaging.outbox;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.codecraft87.eshop.order.messaging.event.OrderCreated;
import io.github.codecraft87.eshop.order.messaging.event.PaymentRequested;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

@Slf4j
@RequiredArgsConstructor
@Service
public class OrderOutboxService {

  private final OrderOutboxRepository outboxRepository;

  private final ObjectMapper objectMapper;

  @Transactional
  public Long saveOrderCreatedEvent(OrderCreated orderCreated) {
    log.info("Saving Order Out box event {} ", orderCreated);
    OrderOutboxMessage outboxEventEntity = buildOrderOutboxMessageEntity(orderCreated);
    return outboxRepository.save(outboxEventEntity).getId();
  }

  @Transactional
  public Long savePaymentRequestEvent(PaymentRequested paymentRequested) {
    log.info("Saving payment requested event {} ", paymentRequested);
    OrderOutboxMessage outBoxMessageEntity = buildPaymentRequestOutboxMessage(paymentRequested);
    return outboxRepository.save(outBoxMessageEntity).getId();
  }

  private OrderOutboxMessage buildPaymentRequestOutboxMessage(PaymentRequested paymentRequest) {
    OrderOutboxMessage outboxMessage = new OrderOutboxMessage();
    outboxMessage.setEventId(UUID.randomUUID());
    outboxMessage.setEventType(OrderEventType.PAYMENT_REQUESTED);
    outboxMessage.setStatus(OrderEventStatus.NEW);
    outboxMessage.setRetryCount(0);
    outboxMessage.setCreatedAt(Instant.now());
    try {
      outboxMessage.setPayload(
          objectMapper.writeValueAsString(
              new PaymentRequested(
                  paymentRequest.orderId(),
                  paymentRequest.paymentMode(),
                  outboxMessage.getEventId().toString())));
    } catch (JacksonException ex) {
      log.error("Failed to serialize PaymentRequest", ex);

      throw new RuntimeException("Failed to serialize PaymentRequest", ex);
    }
    return outboxMessage;
  }

  private OrderOutboxMessage buildOrderOutboxMessageEntity(OrderCreated orderCreated) {
    OrderOutboxMessage outboxEvent = new OrderOutboxMessage();
    outboxEvent.setEventId(UUID.randomUUID());
    outboxEvent.setEventType(OrderEventType.ORDER_CREATED);
    outboxEvent.setStatus(OrderEventStatus.NEW);
    outboxEvent.setRetryCount(0);
    outboxEvent.setCreatedAt(Instant.now());
    try {
      outboxEvent.setPayload(
          objectMapper.writeValueAsString(
              new OrderCreated(
                  orderCreated.basketId(),
                  orderCreated.userId(),
                  outboxEvent.getEventId().toString())));
    } catch (JacksonException ex) {
      log.error("Failed to serialize orderCreatedEvent", ex);

      throw new RuntimeException("Failed to serialize orderCreatedEvent", ex);
    }
    return outboxEvent;
  }

  public List<OrderOutboxMessage> getPendingEvents() {
    return outboxRepository.findByStatusInOrderByCreatedAt(
        List.of(OrderEventStatus.NEW, OrderEventStatus.FAILED));
  }

  @Transactional
  public void save(List<OrderOutboxMessage> events) {
    outboxRepository.saveAll(events);
  }
}
