package io.github.codecraft87.eshop.order.messaging.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class OrderRabbitMQConfig {

  private final TopicExchange topicExchange;

  @Bean
  public Queue getOrderBasketCheckoutQueue() {
    return new Queue(OrderQueueConstants.ORDER_BASKET_CHECKOUT_QUEUE);
  }

  @Bean
  public Queue getBasketOrderCreatedQueue() {
    return new Queue(OrderQueueConstants.BASKET_ORDER_CREATED_QUEUE);
  }

  @Bean
  public Queue getOrderPaymentRequestedQueue() {
    return new Queue(OrderQueueConstants.PAYMENT_ORDER_PAYMENT_REQUESTED_QUEUE);
  }

  @Bean
  public Queue getOrderPaymentCompletedQueue() {
    return new Queue(OrderQueueConstants.ORDER_PAYMENT_COMPLETED_QUEUE);
  }

  @Bean
  public Queue getOrderPaymentFailedQueue() {
    return new Queue(OrderQueueConstants.ORDER_PAYMENT_FAILED_QUEUE);
  }

  @Bean
  public Binding bindBasketOrderQueue() {
    return BindingBuilder.bind(getOrderBasketCheckoutQueue())
        .to(topicExchange)
        .with(OrderRoutingKeyConstants.BASKET_CHECKOUT);
  }

  @Bean
  public Binding bindOrderBasketQueue() {
    return BindingBuilder.bind(getBasketOrderCreatedQueue())
        .to(topicExchange)
        .with(OrderRoutingKeyConstants.ORDER_CREATED);
  }

  @Bean
  public Binding bindOrderPaymentRequestQueue() {
    return BindingBuilder.bind(getOrderPaymentRequestedQueue())
        .to(topicExchange)
        .with(OrderRoutingKeyConstants.ORDER_PAYMENT_REQUESTED);
  }

  @Bean
  public Binding bindOrderPaymentCompletedQueue() {
    return BindingBuilder.bind(getOrderPaymentCompletedQueue())
        .to(topicExchange)
        .with(OrderRoutingKeyConstants.PAYMENT_COMPLETED);
  }

  @Bean
  public Binding bindOrderPaymentFailedQueue() {
    return BindingBuilder.bind(getOrderPaymentFailedQueue())
        .to(topicExchange)
        .with(OrderRoutingKeyConstants.PAYMENT_FAILED);
  }
}
