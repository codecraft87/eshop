package io.github.codecraft87.eshop.payment.messaging.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class PaymentRabbitMQConfig {

  private final TopicExchange topicExchange;

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
  public Binding bindOrderPaymentRequestQueue() {
    return BindingBuilder.bind(getOrderPaymentRequestedQueue())
        .to(topicExchange)
        .with(RoutingKeyConstants.ORDER_PAYMENT_REQUESTED);
  }

  @Bean
  public Binding bindOrderPaymentCompletedQueue() {
    return BindingBuilder.bind(getOrderPaymentCompletedQueue())
        .to(topicExchange)
        .with(RoutingKeyConstants.PAYMENT_COMPLETED);
  }

  @Bean
  public Binding bindOrderPaymentFailedQueue() {
    return BindingBuilder.bind(getOrderPaymentFailedQueue())
        .to(topicExchange)
        .with(RoutingKeyConstants.PAYMENT_FAILED);
  }
}
