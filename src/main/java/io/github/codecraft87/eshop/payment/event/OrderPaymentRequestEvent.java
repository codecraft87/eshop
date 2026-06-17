package io.github.codecraft87.eshop.payment.event;

public record OrderPaymentRequestEvent(
    Long orderId, Boolean simulateSuccess) {
    
    public OrderPaymentRequestEvent {
        if (simulateSuccess == null) {
            simulateSuccess = false;
        }
    }
}
