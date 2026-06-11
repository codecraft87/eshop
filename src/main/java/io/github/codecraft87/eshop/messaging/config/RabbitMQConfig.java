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
    public Queue getOrderBasketCheckoutQueue(){
        return new Queue(QueueConstants.ORDER_BASKET_CHECKOUT_QUEUE);
    }

    @Bean
    public Queue getBasketOrderCreated(){
        return new Queue(QueueConstants.BASKET_ORDER_CREATED_QUEUE);
    }

    @Bean
    public TopicExchange getEShopExchange(){
        return new TopicExchange(ExchangeConstants.ESHOP_EXCHANGE);
    }

    @Bean
    public Binding bindBasketOrderQueue(){
        return  BindingBuilder
                .bind(getOrderBasketCheckoutQueue())
                .to(getEShopExchange())
                .with(RoutingKeyConstants.BASKET_CHECKOUT);
    }

    @Bean
    public Binding bindOrderBasketQueue(){
        return BindingBuilder
                .bind(getBasketOrderCreated())
                .to(getEShopExchange())
                .with(RoutingKeyConstants.ORDER_CREATED);
    }

    @Bean
    public MessageConverter jsonConverter(){
        return new JacksonJsonMessageConverter();
    }

    @Bean
    public AmqpTemplate amqpTemplate(ConnectionFactory connectionFactory){
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonConverter());
        return rabbitTemplate;
    }

}
