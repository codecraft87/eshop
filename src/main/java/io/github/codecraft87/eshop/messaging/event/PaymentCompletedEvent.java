package io.github.codecraft87.eshop.messaging.event;

public record PaymentCompletedEvent(
                        Long orderId, 
                        String eventId) {

}
