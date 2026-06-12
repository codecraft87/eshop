package io.github.codecraft87.eshop.order.publisher;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import io.github.codecraft87.eshop.messaging.config.ExchangeConstants;
import io.github.codecraft87.eshop.messaging.config.RoutingKeyConstants;
import io.github.codecraft87.eshop.messaging.event.OrderCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderCreatedEventPublisher {

    private final RabbitTemplate rabbitTemplate;
 
    public void publishOrderCreatedEvent(OrderCreatedEvent createdEvent){
        log.info("Publishing order created event for basket {} ",createdEvent.basketId());
        rabbitTemplate.convertAndSend(
            ExchangeConstants.ESHOP_EXCHANGE,
            RoutingKeyConstants.ORDER_CREATED,
            createdEvent);
    }
}
