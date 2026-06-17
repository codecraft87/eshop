package io.github.codecraft87.eshop.order.event;

import java.util.UUID;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import io.github.codecraft87.eshop.messaging.config.QueueConstants;
import io.github.codecraft87.eshop.order.idempotency.ProcessedEventService;
import io.github.codecraft87.eshop.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import tools.jackson.databind.ObjectMapper;

@RequiredArgsConstructor
@Slf4j
@Service
public class PaymentFailedConsumer {

    private final OrderService orderService;
    
    private final ObjectMapper objectMapper;
    
    private final ProcessedEventService processedEventService;
    
    
    @RabbitListener(queues = QueueConstants.ORDER_PAYMENT_FAILED_QUEUE)
    public void handlePaymentFailedEvent(
                                String payload) {
        log.info("Received payment failed event ");
        PaymentAckowledge failedEvent = objectMapper.readValue(
                payload, PaymentAckowledge.class);
        if(failedEvent!=null) {
            UUID eventId = UUID.fromString(failedEvent.eventId());
            if(processedEventService.checkIfEventIsProcessed(eventId)) {
                log.info(
                        "Duplicate event {} ignored",
                        eventId);
                return;
            }
            orderService.markOrderAsFailed(failedEvent.orderId());
            processedEventService.addProcessedEventEntry(eventId);
        }
    }
}
