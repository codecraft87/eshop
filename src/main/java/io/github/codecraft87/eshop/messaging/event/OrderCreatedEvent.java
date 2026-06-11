package io.github.codecraft87.eshop.messaging.event;

public record OrderCreatedEvent(
            Long basketId, 
            Long userId) {
}
