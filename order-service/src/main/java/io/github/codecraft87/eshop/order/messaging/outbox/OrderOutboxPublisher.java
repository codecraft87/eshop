package io.github.codecraft87.eshop.order.messaging.outbox;

import java.util.List;

import org.springframework.amqp.AmqpException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import io.github.codecraft87.eshop.messagingcommon.config.ExchangeConstants;
import io.github.codecraft87.eshop.messagingcommon.publishing.EventPublisher;
import io.github.codecraft87.eshop.order.messaging.config.OrderRoutingKeyConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Component
@Slf4j
public class OrderOutboxPublisher {

    private final OrderOutboxService outboxService;

    private final EventPublisher eventPublisher;

    @Transactional
    public void publishPendingEvents() {

        List<OrderOutboxMessage> events = outboxService.getPendingEvents();

        if (events.size() > 0)
            log.info("Pending order events to publish {} ", events.size());

        for (OrderOutboxMessage event : events) {
            try {
                switch (event.getEventType()) {
                    case OrderEventType.ORDER_CREATED:
                        publishOrderCreatedEvent(event.getPayload());
                        break;
                    case OrderEventType.PAYMENT_REQUESTED:
                        publishPaymentRequestedEvent(event.getPayload());
                        break;
                    default:
                        log.warn("unknown event type to handle");
                }
                event.markPublished();
            } catch (AmqpException ex) {
                log.error("Event published failed ", ex);
                event.markFailed(ex.getMessage());
            }
            if (events.size() > 0) {
                outboxService.save(events);
                log.info("events saved");
            }
        }
    }

    private void publishOrderCreatedEvent(String payload) {
        eventPublisher.publish(
                ExchangeConstants.ESHOP_EXCHANGE, OrderRoutingKeyConstants.ORDER_CREATED, payload);
    }

    private void publishPaymentRequestedEvent(String payload) {
        eventPublisher.publish(
                ExchangeConstants.ESHOP_EXCHANGE, OrderRoutingKeyConstants.ORDER_PAYMENT_REQUESTED, payload);
    }
}
