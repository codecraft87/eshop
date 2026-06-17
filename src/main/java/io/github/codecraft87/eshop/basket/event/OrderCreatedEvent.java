package io.github.codecraft87.eshop.basket.event;

public record OrderCreatedEvent(
            String eventId,
            Long basketId, 
            Long userId) {
}
