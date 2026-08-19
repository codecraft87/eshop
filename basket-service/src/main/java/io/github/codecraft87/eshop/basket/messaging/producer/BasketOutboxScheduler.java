package io.github.codecraft87.eshop.basket.messaging.producer;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import io.github.codecraft87.eshop.basket.messaging.outbox.BasketOutboxPublisher;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class BasketOutboxScheduler {

  private final BasketOutboxPublisher eventPublisher;

  @Scheduled(fixedDelay = 5000)
  public void publishEvents() {
    eventPublisher.publishPendingEvents();
  }
}
