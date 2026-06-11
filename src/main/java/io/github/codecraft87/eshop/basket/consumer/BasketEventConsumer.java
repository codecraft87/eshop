package io.github.codecraft87.eshop.basket.consumer;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import io.github.codecraft87.eshop.basket.service.BasketService;
import io.github.codecraft87.eshop.messaging.config.QueueConstants;
import io.github.codecraft87.eshop.messaging.event.OrderCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class BasketEventConsumer {

    private final BasketService basketService;

    @RabbitListener(queues = QueueConstants.BASKET_ORDER_CREATED_QUEUE)
    public void orderCeated(OrderCreatedEvent createdEvent){
        log.info("Received OrderCreatedEvent for basket {}", createdEvent.basketId());
        basketService.updateBasketForOrder(createdEvent.basketId());
        log.info("Basket updated for status");
    }
}
