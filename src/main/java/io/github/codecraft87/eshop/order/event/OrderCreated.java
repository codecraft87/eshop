package io.github.codecraft87.eshop.order.event;

public record OrderCreated(Long basketId, Long userId, String eventId) {}
