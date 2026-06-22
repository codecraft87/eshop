package io.github.codecraft87.eshop.messaging.config;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

  @Bean
  public TopicExchange getEShopExchange() {
    return new TopicExchange(ExchangeConstants.ESHOP_EXCHANGE);
  }

  @Bean
  public Queue getOrderBasketCheckoutQueue() {
    return new Queue(QueueConstants.ORDER_BASKET_CHECKOUT_QUEUE);
  }

  @Bean
  public Queue getBasketOrderCreatedQueue() {
    return new Queue(QueueConstants.BASKET_ORDER_CREATED_QUEUE);
  }

  @Bean
  public Queue getOrderPaymentRequestedQueue() {
    return new Queue(QueueConstants.PAYMENT_ORDER_PAYMENT_REQUESTED_QUEUE);
  }

  @Bean
  public Queue getOrderPaymentCompletedQueue() {
    return new Queue(QueueConstants.ORDER_PAYMENT_COMPLETED_QUEUE);
  }

  @Bean
  public Queue getOrderPaymentFailedQueue() {
    return new Queue(QueueConstants.ORDER_PAYMENT_FAILED_QUEUE);
  }

  @Bean
  public Binding bindBasketOrderQueue() {
    return BindingBuilder.bind(getOrderBasketCheckoutQueue())
        .to(getEShopExchange())
        .with(RoutingKeyConstants.BASKET_CHECKOUT);
  }

  @Bean
  public Binding bindOrderBasketQueue() {
    return BindingBuilder.bind(getBasketOrderCreatedQueue())
        .to(getEShopExchange())
        .with(RoutingKeyConstants.ORDER_CREATED);
  }

  @Bean
  public Binding bindOrderPaymentRequestQueue() {
    return BindingBuilder.bind(getOrderPaymentRequestedQueue())
        .to(getEShopExchange())
        .with(RoutingKeyConstants.ORDER_PAYMENT_REQUESTED);
  }

  @Bean
  public Binding bindOrderPaymentCompletedQueue() {
    return BindingBuilder.bind(getOrderPaymentCompletedQueue())
        .to(getEShopExchange())
        .with(RoutingKeyConstants.PAYMENT_COMPLETED);
  }

  @Bean
  public Binding bindOrderPaymentFailedQueue() {
    return BindingBuilder.bind(getOrderPaymentFailedQueue())
        .to(getEShopExchange())
        .with(RoutingKeyConstants.PAYMENT_FAILED);
  }

  @Bean
  public MessageConverter jsonConverter() {
    return new JacksonJsonMessageConverter();
  }

  @Bean
  public AmqpTemplate amqpTemplate(ConnectionFactory connectionFactory) {
    RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
    rabbitTemplate.setMessageConverter(jsonConverter());
    return rabbitTemplate;
  }
}
