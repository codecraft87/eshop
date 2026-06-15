package io.github.codecraft87.eshop.basket.publisher;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import io.github.codecraft87.eshop.basket.service.BasketOutboxEventService;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class BasketOutboxScheduler {

    private final BasketOutboxEventService outboxService;
    @Scheduled(fixedDelay = 5000)
    public void publishEvents() {
      
        outboxService.publishPendingEvents();
    }
}
