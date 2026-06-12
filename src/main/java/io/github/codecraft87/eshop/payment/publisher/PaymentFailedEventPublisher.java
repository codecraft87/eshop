package io.github.codecraft87.eshop.payment.publisher;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import io.github.codecraft87.eshop.messaging.config.ExchangeConstants;
import io.github.codecraft87.eshop.messaging.config.RoutingKeyConstants;
import io.github.codecraft87.eshop.messaging.event.PaymentFailedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@Service
public class PaymentFailedEventPublisher {

    private final RabbitTemplate rabbitTemplate;
    
    public void publishPaymentFailedEvent(
            PaymentFailedEvent paymentFailedEvent) {
        log.info("Publishing payment failed event {}", paymentFailedEvent);
        rabbitTemplate.convertAndSend(
                ExchangeConstants.ESHOP_EXCHANGE, 
                RoutingKeyConstants.PAYMENT_FAILED, 
                paymentFailedEvent);
    }
}
