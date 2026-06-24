package io.github.codecraft87.eshop.basket.event;

public record BasketItemEvent(Long productId, String productName, Double price, Integer quantity) {}
