package io.github.codecraft87.eshop.messagingcommon.rabbitmq;

import org.springframework.amqp.rabbit.core.RabbitTemplate;

import io.github.codecraft87.eshop.messagingcommon.publishing.EventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public class RabbitMqEventPublisher implements EventPublisher {

    private final RabbitTemplate rabbitTemplate;

    @Override
    public void publish(String exchange, String routingKey, String payload) {
        rabbitTemplate.convertAndSend(exchange, routingKey, payload);
        log.info("event published");
    }
}
