package io.github.codecraft87.eshop.basket.messaging.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class BasketRabbitMQConfig {

  private final TopicExchange topicExchange;

  @Bean
  public Queue getOrderBasketCheckoutQueue() {
    return new Queue(QueueConstants.ORDER_BASKET_CHECKOUT_QUEUE);
  }

  @Bean
  public Queue getBasketOrderCreatedQueue() {
    return new Queue(QueueConstants.BASKET_ORDER_CREATED_QUEUE);
  }

  @Bean
  public Binding bindBasketOrderQueue() {
    return BindingBuilder.bind(getOrderBasketCheckoutQueue())
        .to(topicExchange)
        .with(RoutingKeyConstants.BASKET_CHECKOUT);
  }

  @Bean
  public Binding bindOrderBasketQueue() {
    return BindingBuilder.bind(getBasketOrderCreatedQueue())
        .to(topicExchange)
        .with(RoutingKeyConstants.ORDER_CREATED);
  }
}
