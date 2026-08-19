package io.github.codecraft87.eshop.payment.messaging.outbox;

import java.util.List;

import org.springframework.amqp.AmqpException;
import org.springframework.stereotype.Component;

import io.github.codecraft87.eshop.messagingcommon.config.ExchangeConstants;
import io.github.codecraft87.eshop.messagingcommon.publishing.EventPublisher;
import io.github.codecraft87.eshop.payment.messaging.config.RoutingKeyConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Component
@Slf4j
public class PaymentOutboxPublisher {

    private final PaymentOutboxService paymentOutboxService;

    private final EventPublisher eventPublisher;

    public void publishPendingEvents() {

        List<PaymentOutboxMessage> events = paymentOutboxService.getPendingEvents();

        if (events.size() > 0)
            log.info("Pending payment events to publish {} ", events.size());
        for (PaymentOutboxMessage event : events) {
            try {
                switch (event.getEventType()) {
                    case PaymentEventType.PAYMENT_DONE:
                        publishPaymentDoneEvent(event.getPayload());
                        event.markPublished();
                        break;
                    case PaymentEventType.PAYMENT_FAILED:
                        publishPaymentFailedEvent(event.getPayload());
                        event.markPublished();
                        break;
                    default:
                        log.info("unknown event type to handle");
                }
            } catch (AmqpException ex) {
                log.error("Event published failed ", ex);
                event.markFailed(ex.getMessage());
            }
        }
        if (events.size() > 0) {
            paymentOutboxService.save(events);
            log.info("events saved");
        }
    }

    private void publishPaymentFailedEvent(String payload) {
        eventPublisher.publish(ExchangeConstants.ESHOP_EXCHANGE,
                RoutingKeyConstants.PAYMENT_FAILED, payload);
    }

    private void publishPaymentDoneEvent(String payload) {
        eventPublisher.publish(ExchangeConstants.ESHOP_EXCHANGE,
                RoutingKeyConstants.PAYMENT_COMPLETED, payload);
    }
}
