package io.github.codecraft87.eshop.order.outbox;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import io.github.codecraft87.eshop.messaging.config.ExchangeConstants;
import io.github.codecraft87.eshop.messaging.config.RoutingKeyConstants;
import io.github.codecraft87.eshop.order.event.OrderCreated;
import io.github.codecraft87.eshop.order.event.PaymentRequested;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

@Slf4j
@RequiredArgsConstructor
@Service
public class OrderOutboxService {

    private final OrderOutboxRepository outboxRepository;
    
    private final RabbitTemplate rabbitTemplate;
    
    private ObjectMapper objectMapper;
    
    public Long saveOrderCreatedEvent(OrderCreated orderCreated) {
      log.info("Saving Order Out box event {} ", orderCreated);
      OrderOutboxMessage outboxEventEntity = buildOrderOutboxMessageEntity(orderCreated);
      return outboxRepository.save(outboxEventEntity).getId();
    }
    
    public Long savePaymentRequestEvent(
                    PaymentRequested paymentRequested) {
        log.info("Saving payment requested event {} ", paymentRequested);
        OrderOutboxMessage outBoxMessageEntity = buildPaymentRequestOutboxMessage(paymentRequested);
        return outboxRepository.save(outBoxMessageEntity).getId();
    }

    private OrderOutboxMessage buildPaymentRequestOutboxMessage(
                                                        PaymentRequested paymentRequest) {
            log.info("Build outbox message");
            OrderOutboxMessage outboxMessage = new OrderOutboxMessage();
            outboxMessage.setEventId(UUID.randomUUID());
            outboxMessage.setEventType(OrderEventType.ORDER_CREATED);
            outboxMessage.setStatus(OrderEventStatus.NEW);
            outboxMessage.setRetryCount(0);
            outboxMessage.setCreatedAt(Instant.now());
            try {
                outboxMessage.setPayload(
                        objectMapper.writeValueAsString(
                                new PaymentRequested(
                                        paymentRequest.orderId(),
                                        paymentRequest.simulate(),
                                        outboxMessage.getEventId().toString())));
            }catch (JacksonException ex) {
                log.error(
                        "Failed to serialize PaymentRequest",
                        ex);

                
                throw new RuntimeException(
                        "Failed to serialize PaymentRequest",
                        ex);
            }
            return outboxMessage;
    }
        
    private OrderOutboxMessage buildOrderOutboxMessageEntity(OrderCreated orderCreated) {
        OrderOutboxMessage outboxEvent = new OrderOutboxMessage();
          outboxEvent.setEventId(UUID.randomUUID());
          outboxEvent.setEventType(OrderEventType.ORDER_CREATED);
          outboxEvent.setStatus(OrderEventStatus.NEW);
          outboxEvent.setRetryCount(0);
          outboxEvent.setCreatedAt(Instant.now());
          try {
              outboxEvent.setPayload(objectMapper.writeValueAsString(
                      new OrderCreated(orderCreated.basketId(), 
                              orderCreated.userId(),
                              outboxEvent.getEventId().toString())));
          }catch (JacksonException ex) {
                log.error(
                        "Failed to serialize orderCreatedEvent",
                        ex);

                
                throw new RuntimeException(
                        "Failed to serialize orderCreatedEvent",
                        ex);
            }
        return outboxEvent;
    }
    
    public void publishPendingEvents() {
        log.info("Publishing pending events ");
        List<OrderOutboxMessage> events = outboxRepository
                .findByStatusInOrderByCreatedAt(
                        List.of(
                                OrderEventStatus.NEW, 
                                OrderEventStatus.FAILED));
        log.info("Found pending events to publish {} ", events.size());

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
                    log.info("unknown event type to handle");
                }
                log.info("event published and marked as published");
                event.markPublished();
            } catch (AmqpException ex) {
                log.error("Event published failed ", ex);
                event.markFailed(ex.getMessage());
            }
            if (events.size() > 0) {
                log.info("Publishing event {} ", event.getEventId().toString());
                outboxRepository.saveAll(events);
                log.info("events saved");
            }
        }
    }

    private void publishOrderCreatedEvent(String payload) {
        log.info("Publishing order created event ");
        rabbitTemplate.convertAndSend(
                ExchangeConstants.ESHOP_EXCHANGE,
                RoutingKeyConstants.ORDER_CREATED,
                payload);
    }
    private void publishPaymentRequestedEvent(String payload) {
        log.info("Publishing order created event ");
        rabbitTemplate.convertAndSend(
                ExchangeConstants.ESHOP_EXCHANGE,
                RoutingKeyConstants.ORDER_PAYMENT_REQUESTED,
                payload);
        
    }
}
