package io.github.codecraft87.eshop.order.messaging.event;

public record OrderCreated(Long basketId, Long userId, String eventId) {}
