package io.github.codecraft87.eshop.order.messaging.event;

public record BasketItemEvent(
    Long productId, String productName, Double price, Integer quantity) {}
