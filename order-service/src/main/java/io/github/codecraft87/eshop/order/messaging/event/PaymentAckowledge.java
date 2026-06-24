package io.github.codecraft87.eshop.order.messaging.event;

public record PaymentAckowledge(Long orderId, String eventId) {}
