package io.github.codecraft87.eshop.payment.outbox;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PaymentOutboxScheduler {

    private final PaymentOutboxService outboxEventService;
    
    @Scheduled(fixedDelay = 5000)
    public void publishEvents() {
      
        outboxEventService.publishPendingEvents();
    }
}
