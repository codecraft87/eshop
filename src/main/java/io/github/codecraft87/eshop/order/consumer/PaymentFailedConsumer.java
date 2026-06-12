package io.github.codecraft87.eshop.order.consumer;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import io.github.codecraft87.eshop.messaging.config.QueueConstants;
import io.github.codecraft87.eshop.messaging.event.PaymentFailedEvent;
import io.github.codecraft87.eshop.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@Service
public class PaymentFailedConsumer {

    private final OrderService orderService;
    
    @RabbitListener(queues = QueueConstants.ORDER_PAYMENT_FAILED_QUEUE)
    public void handlePaymentFailedEvent(
        PaymentFailedEvent paymentFailedEvent) {
        log.info("Received payment failed event {}", paymentFailedEvent);
        orderService.markOrderAsFailed(paymentFailedEvent.orderId());
    }
}
