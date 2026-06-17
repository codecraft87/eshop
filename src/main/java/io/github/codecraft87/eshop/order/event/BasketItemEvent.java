package io.github.codecraft87.eshop.order.event;

public record BasketItemEvent(
                    Long productId,
                    Integer quantity) {
}
