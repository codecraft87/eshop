package io.github.codecraft87.eshop.order.idempotency;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import io.github.codecraft87.eshop.messaging.config.QueueConstants;
import io.github.codecraft87.eshop.messaging.event.OrderPaymentRequestEvent;
import io.github.codecraft87.eshop.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Service
@Slf4j
public class OrderPaymentCompletedConsumer {

    private final OrderService orderService;
    
    @RabbitListener(queues = QueueConstants.ORDER_PAYMENT_COMPLETED_QUEUE)
    public void handlePaymentRequestedEvent(OrderPaymentRequestEvent event) {
        log.info("Received Payment requested event");
        orderService.updateOrderForPayment(event.orderId());
    }
}
