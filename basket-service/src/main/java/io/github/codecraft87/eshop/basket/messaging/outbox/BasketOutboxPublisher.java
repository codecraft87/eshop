package io.github.codecraft87.eshop.basket.messaging.outbox;

import java.util.List;

import org.springframework.amqp.AmqpException;
import org.springframework.stereotype.Component;

import io.github.codecraft87.eshop.basket.messaging.config.RoutingKeyConstants;
import io.github.codecraft87.eshop.messagingcommon.config.ExchangeConstants;
import io.github.codecraft87.eshop.messagingcommon.publishing.EventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Component
@Slf4j
public class BasketOutboxPublisher {

    private final BasketOutboxService basketOutboxService;

    private final EventPublisher eventPublisher;

    public void publishPendingEvents() {

        List<BasketOutboxMessage> events = basketOutboxService.getPendingEvents();

        if (events.size() > 0)
            log.info("Pending basket events to publish {} ", events.size());

        for (BasketOutboxMessage event : events) {

            try {
                log.info("Publishing event {} ", event.getEventId().toString());
                eventPublisher.publish(ExchangeConstants.ESHOP_EXCHANGE,
                        RoutingKeyConstants.BASKET_CHECKOUT,
                        event.getPayload());
                log.info("event published and marked as published");
                event.markPublished();

            } catch (AmqpException ex) { // need exception handler
                log.error("Event published failed ", ex);
                event.markFailed(ex.getMessage());
            }
        }
        if (events.size() > 0) {
            basketOutboxService.save(events);
            log.info("events saved");
        }
    }
}
