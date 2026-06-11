package io.github.codecraft87.eshop.messaging.event;

public record BasketItemEvent(Long productId,
        Integer quantity) {
}
