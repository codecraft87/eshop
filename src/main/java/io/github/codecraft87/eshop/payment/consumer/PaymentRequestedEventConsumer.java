package io.github.codecraft87.eshop.payment.consumer;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import io.github.codecraft87.eshop.messaging.config.QueueConstants;
import io.github.codecraft87.eshop.messaging.event.OrderPaymentRequestEvent;
import io.github.codecraft87.eshop.payment.dto.PaymentRequest;
import io.github.codecraft87.eshop.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@Service
public class PaymentRequestedEventConsumer {

    private final PaymentService paymentService;
    
    @RabbitListener(queues = QueueConstants.PAYMENT_ORDER_PAYMENT_REQUESTED_QUEUE)
    public void handlePaymentRequested(OrderPaymentRequestEvent event) {
      log.info("Received Order payment requested {} ",event);
      PaymentRequest request = new PaymentRequest();
      request.setOrderId(event.orderId());
      request.setSimulateFailure(event.simulateSuccess());
      paymentService.processPayment(request);
    }
}
