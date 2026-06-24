package io.github.codecraft87.eshop.basket.messaging.event;

public record OrderCreatedEvent(String eventId, Long basketId, Long userId) {}
