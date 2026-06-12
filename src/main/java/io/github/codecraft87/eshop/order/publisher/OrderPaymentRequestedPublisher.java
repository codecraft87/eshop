package io.github.codecraft87.eshop.order.publisher;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import io.github.codecraft87.eshop.messaging.config.ExchangeConstants;
import io.github.codecraft87.eshop.messaging.config.RoutingKeyConstants;
import io.github.codecraft87.eshop.messaging.event.OrderPaymentRequestEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@Service
public class OrderPaymentRequestedPublisher {

    private final RabbitTemplate rabbitTemplate;
    
    public void publishOrderPaymentRequestedEvent(
                        OrderPaymentRequestEvent event) {
        log.info("Publishing order payment requested event {} ", event);
        rabbitTemplate.convertAndSend(
                ExchangeConstants.ESHOP_EXCHANGE,
                RoutingKeyConstants.ORDER_PAYMENT_REQUESTED,
                event);
    }
}
