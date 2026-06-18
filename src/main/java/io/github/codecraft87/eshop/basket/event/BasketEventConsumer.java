package io.github.codecraft87.eshop.basket.event;

import java.util.UUID;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import io.github.codecraft87.eshop.basket.idempotency.BasketProcessedEventService;
import io.github.codecraft87.eshop.basket.service.BasketService;
import io.github.codecraft87.eshop.messaging.config.QueueConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import tools.jackson.databind.ObjectMapper;

@Slf4j
@Service
@RequiredArgsConstructor
public class BasketEventConsumer {

    private final BasketService basketService;
    
    private final ObjectMapper objectMapper;
    
    private final BasketProcessedEventService processedEventService;
    
    @RabbitListener(queues = QueueConstants.BASKET_ORDER_CREATED_QUEUE)
    public void handleOrderCreatedEvent(String payload){
        log.info("Received OrderCreatedEvent ");
        OrderCreatedEvent createdEvent = objectMapper.readValue(
                payload, OrderCreatedEvent.class);
        
        log.info("The basket {}", createdEvent.basketId());
        if(createdEvent!=null) {
            UUID eventId = UUID.fromString(createdEvent.eventId());
           if(processedEventService.checkIfEventIsProcessed(eventId)) {
               log.info("Duplicate event {} ignored ", 
                       eventId);
               return;
           }
           basketService.updateBasketForOrder(createdEvent.basketId());
           processedEventService.addProcessedEventEntry(eventId);
        }
        log.info("Basket updated for status");
    }
}
