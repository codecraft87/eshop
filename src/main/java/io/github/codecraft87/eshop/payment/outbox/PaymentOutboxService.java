package io.github.codecraft87.eshop.payment.outbox;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import io.github.codecraft87.eshop.messaging.config.ExchangeConstants;
import io.github.codecraft87.eshop.messaging.config.RoutingKeyConstants;
import io.github.codecraft87.eshop.payment.event.PaymentAckowledge;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentOutboxService {
    
    private final PaymentOutboxRepository outboxRepository;
    
    private final RabbitTemplate rabbitTemplate;
    
    private final ObjectMapper objectMapper;
    
    public Long savePaymentCompletedEvent(Long orderId) {
        log.info("Saving Payment Requested Out box event {} ", orderId);
        PaymentOutboxMessage outboxMessageEntity = buildPaymentCompletedMessageEntity(orderId);
        return outboxRepository.save(outboxMessageEntity).getId();
    }

    private PaymentOutboxMessage buildPaymentCompletedMessageEntity(Long orderId) {
        log.info("Build outbox message");
        PaymentOutboxMessage outboxMessage = new PaymentOutboxMessage();
        outboxMessage.setEventId(UUID.randomUUID());
        outboxMessage.setEventType(PaymentEventType.PAYMENT_DONE);
        outboxMessage.setStatus(PaymentEventStatus.NEW);
        outboxMessage.setRetryCount(0);
        outboxMessage.setCreatedAt(Instant.now());
        try {
            outboxMessage.setPayload(
                    objectMapper
                        .writeValueAsString(
                                   new PaymentAckowledge(
                                           orderId, 
                                           outboxMessage.getEventId().toString())));
            
        }catch (JacksonException ex) {
            log.error(
                    "Failed to serialize Payment Completed Request",
                    ex);

            
            throw new RuntimeException(
                    "Failed to serialize Payment Completed Request",
                    ex);
        }
        return outboxMessage;
    }
    
    public Long savePaymentFailededEvent(Long orderId) {
        log.info("Saving Payment Requested Out box event {} ", orderId);
        PaymentOutboxMessage outboxMessageEntity = 
                buildPaymentFailedMessage(orderId);
        return outboxRepository.save(outboxMessageEntity).getId();
    }
    
    private PaymentOutboxMessage buildPaymentFailedMessage(Long orderId) {
        log.info("Build outbox message");
        PaymentOutboxMessage outboxMessage = new PaymentOutboxMessage();
        outboxMessage.setEventId(UUID.randomUUID());
        outboxMessage.setEventType(PaymentEventType.PAYMENT_FAILED);
        outboxMessage.setStatus(PaymentEventStatus.NEW);
        outboxMessage.setRetryCount(0);
        outboxMessage.setCreatedAt(Instant.now());
        try {
            outboxMessage.setPayload(
                    objectMapper
                        .writeValueAsString(
                                   new PaymentAckowledge(
                                           orderId, 
                                           outboxMessage.getEventId().toString())));
            
        }catch (JacksonException ex) {
            log.error(
                    "Failed to serialize Payment Completed Request",
                    ex);

            
            throw new RuntimeException(
                    "Failed to serialize Payment Completed Request",
                    ex);
        }
        return outboxMessage;
    }

    public void publishPendingEvents() {
        log.info("Publishing pending events ");
        List<PaymentOutboxMessage> events = outboxRepository.
                findByStatusInOrderByCreatedAt(
                        List.of(
                                PaymentEventStatus.NEW,
                                PaymentEventStatus.FAILED));
        for(PaymentOutboxMessage event: events) {
            try {
                switch(event.getEventType()) {
                case PaymentEventType.PAYMENT_DONE:
                    publishPaymentDoneEvent(event.getPayload());
                    break;
                case PaymentEventType.PAYMENT_FAILED:
                    publishPaymentFailedEvent(event.getPayload());
                    break;
                default:
                    log.info("unknown event type to handle");
              }
            }catch (AmqpException ex) {
                log.error("Event published failed ", ex);
                event.markFailed(ex.getMessage());
            }
        }
    }

    private void publishPaymentFailedEvent(String payload) {
        log.info("Publishing order created event ");
        rabbitTemplate.convertAndSend(
                ExchangeConstants.ESHOP_EXCHANGE,
                RoutingKeyConstants.PAYMENT_FAILED,
                payload);
        
    }

    private void publishPaymentDoneEvent(String payload) {
        log.info("Publishing order created event ");
        rabbitTemplate.convertAndSend(
                ExchangeConstants.ESHOP_EXCHANGE,
                RoutingKeyConstants.PAYMENT_COMPLETED,
                payload);
    }
}
