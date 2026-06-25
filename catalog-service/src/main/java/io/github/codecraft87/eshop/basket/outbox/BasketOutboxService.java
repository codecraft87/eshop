package io.github.codecraft87.eshop.basket.outbox;

import java.util.List;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import io.github.codecraft87.eshop.messaging.config.ExchangeConstants;
import io.github.codecraft87.eshop.messaging.config.RoutingKeyConstants;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class BasketOutboxService {

  private final BasketOutboxRepository outboxRepository;

  private final RabbitTemplate rabbitTemplate;

  @Transactional
  public Long saveBasketOutboxEvent(BasketOutboxMessage outboxEvent) {
    log.info("Saving basket out box event {} ", outboxEvent);
    return outboxRepository.save(outboxEvent).getId();
  }

  @Transactional
  public void publishPendingEvents() {

    List<BasketOutboxMessage> events =
        outboxRepository.findByStatusInOrderByCreatedAt(
            List.of(OutboxEventStatus.NEW, OutboxEventStatus.FAILED));
    if (events.size() > 0) log.info("Pending basket events to publish {} ", events.size());

    for (BasketOutboxMessage event : events) {

      try {
        log.info("Publishing event {} ", event.getEventId().toString());
        rabbitTemplate.convertAndSend(
            ExchangeConstants.ESHOP_EXCHANGE,
            RoutingKeyConstants.BASKET_CHECKOUT,
            event.getPayload());
        log.info("event published and marked as published");
        event.markPublished();

      } catch (AmqpException ex) {
        log.error("Event published failed ", ex);
        event.markFailed(ex.getMessage());
      }
    }
    if (events.size() > 0) {
      outboxRepository.saveAll(events);
      log.info("events saved");
    }
  }
}
