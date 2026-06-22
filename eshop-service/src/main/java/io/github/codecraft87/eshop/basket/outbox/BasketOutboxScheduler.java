package io.github.codecraft87.eshop.basket.outbox;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class BasketOutboxScheduler {

  private final BasketOutboxService outboxService;

  @Scheduled(fixedDelay = 5000)
  public void publishEvents() {

    outboxService.publishPendingEvents();
  }
}
