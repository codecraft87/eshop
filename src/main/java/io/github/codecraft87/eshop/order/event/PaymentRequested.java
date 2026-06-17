package io.github.codecraft87.eshop.order.event;

public record PaymentRequested(
                        Long orderId,
                        Boolean simulate,
                        String eventId) {

}
