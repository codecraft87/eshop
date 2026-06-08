package io.github.codecraft87.eshop.basket.mapper;

import io.github.codecraft87.eshop.basket.dto.BasketResponse;
import io.github.codecraft87.eshop.basket.entity.BasketItem;

public class BasketMapper {
    public static BasketResponse getBasketItemResponse(
        BasketItem basketItem) {
        return BasketResponse.builder()
                .productId(
                        basketItem.getProduct().getId())
                .productName(
                        basketItem.getProduct().getName())
                .description(
                        basketItem.getProduct().getDescription())
                .price(
                        basketItem.getProduct().getPrice())
                .quantity(
                        basketItem.getQuantity())
                .build();
    }
}
