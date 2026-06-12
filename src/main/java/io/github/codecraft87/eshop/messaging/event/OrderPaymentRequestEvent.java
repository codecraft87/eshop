package io.github.codecraft87.eshop.messaging.event;

public record OrderPaymentRequestEvent(
    Long orderId, Boolean simulateSuccess) {
    
    public OrderPaymentRequestEvent {
        if (simulateSuccess == null) {
            simulateSuccess = false;
        }
    }
}
