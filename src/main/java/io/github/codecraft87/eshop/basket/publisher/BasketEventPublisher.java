package io.github.codecraft87.eshop.basket.publisher;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import io.github.codecraft87.eshop.messaging.config.ExchangeConstants;
import io.github.codecraft87.eshop.messaging.config.RoutingKeyConstants;
import io.github.codecraft87.eshop.messaging.event.BasketCheckedOutEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class BasketEventPublisher {

    private final RabbitTemplate rabbitTemplate;
 
    public void sendBasketCheckedOutEvent(BasketCheckedOutEvent event){
        log.info("Publishing Basket Checked Out Event for basket {}", event.basketId());
        rabbitTemplate.convertAndSend(
            ExchangeConstants.ESHOP_EXCHANGE,
            RoutingKeyConstants.BASKET_CHECKOUT,
            event);
        log.info("BasketCheckedOutEvent published");
    }
}
