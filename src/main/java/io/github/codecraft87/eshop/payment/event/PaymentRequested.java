package io.github.codecraft87.eshop.payment.event;

public record PaymentRequested(
                        Long orderId,
                        Boolean simulate,
                        String eventId) {

}
