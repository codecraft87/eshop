package io.github.codecraft87.eshop.order.event;

public record PaymentAckowledge(Long orderId, String eventId) {}
