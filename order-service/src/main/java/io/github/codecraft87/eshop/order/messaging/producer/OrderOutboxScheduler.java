package io.github.codecraft87.eshop.order.messaging.producer;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import io.github.codecraft87.eshop.order.messaging.outbox.OrderOutboxPublisher;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OrderOutboxScheduler {

  private final OrderOutboxPublisher outboxEventPublisher;

  @Scheduled(fixedDelay = 5000)
  public void publishEvents() {
    outboxEventPublisher.publishPendingEvents();
  }
}
