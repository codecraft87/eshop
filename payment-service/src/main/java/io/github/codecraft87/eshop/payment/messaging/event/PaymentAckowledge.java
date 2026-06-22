package io.github.codecraft87.eshop.payment.messaging.event;

public record PaymentAckowledge(Long orderId, String eventId) {}
