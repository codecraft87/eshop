package io.github.codecraft87.eshop.payment.messaging.outbox;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.codecraft87.eshop.messagingcommon.config.ExchangeConstants;
import io.github.codecraft87.eshop.payment.messaging.config.RoutingKeyConstants;
import io.github.codecraft87.eshop.payment.messaging.event.PaymentAckowledge;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentOutboxService {

  private final PaymentOutboxRepository outboxRepository;

  private final ObjectMapper objectMapper;

  @Transactional
  public Long savePaymentCompletedEvent(Long orderId) {
    log.info("Saving Payment Requested Out box event {} ", orderId);
    PaymentOutboxMessage outboxMessageEntity = buildPaymentCompletedMessageEntity(orderId);
    return outboxRepository.save(outboxMessageEntity).getId();
  }

  private PaymentOutboxMessage buildPaymentCompletedMessageEntity(Long orderId) {
    PaymentOutboxMessage outboxMessage = new PaymentOutboxMessage();
    outboxMessage.setEventId(UUID.randomUUID());
    outboxMessage.setEventType(PaymentEventType.PAYMENT_DONE);
    outboxMessage.setStatus(PaymentEventStatus.NEW);
    outboxMessage.setRetryCount(0);
    outboxMessage.setCreatedAt(Instant.now());
    try {
      outboxMessage.setPayload(
          objectMapper.writeValueAsString(
              new PaymentAckowledge(orderId, outboxMessage.getEventId().toString())));

    } catch (JacksonException ex) {
      log.error("Failed to serialize Payment Completed Request", ex);

      throw new RuntimeException("Failed to serialize Payment Completed Request", ex);
    }
    return outboxMessage;
  }

  @Transactional
  public Long savePaymentFailededEvent(Long orderId) {
    log.info("Saving Payment Requested Out box event {} ", orderId);
    PaymentOutboxMessage outboxMessageEntity = buildPaymentFailedMessage(orderId);
    return outboxRepository.save(outboxMessageEntity).getId();
  }

  private PaymentOutboxMessage buildPaymentFailedMessage(Long orderId) {
    PaymentOutboxMessage outboxMessage = new PaymentOutboxMessage();
    outboxMessage.setEventId(UUID.randomUUID());
    outboxMessage.setEventType(PaymentEventType.PAYMENT_FAILED);
    outboxMessage.setStatus(PaymentEventStatus.NEW);
    outboxMessage.setRetryCount(0);
    outboxMessage.setCreatedAt(Instant.now());
    try {
      outboxMessage.setPayload(
          objectMapper.writeValueAsString(
              new PaymentAckowledge(orderId, outboxMessage.getEventId().toString())));

    } catch (JacksonException ex) {
      log.error("Failed to serialize Payment Completed Request", ex);

      throw new RuntimeException("Failed to serialize Payment Completed Request", ex);
    }
    return outboxMessage;
  }

  @Transactional(readOnly = true)
  public List<PaymentOutboxMessage> getPendingEvents() {
    return outboxRepository.findByStatusInOrderByCreatedAt(
        List.of(PaymentEventStatus.NEW, PaymentEventStatus.FAILED));
  }

  @Transactional
  public void save(List<PaymentOutboxMessage> events) {
    outboxRepository.saveAll(events);
  }
}
