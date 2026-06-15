package io.github.codecraft87.eshop.basket.mapper;

import io.github.codecraft87.eshop.basket.dto.BasketResponse;
import io.github.codecraft87.eshop.basket.entity.BasketItem;

public class BasketMapper {
    public static BasketResponse toBasketItemResponse(
            BasketItem basket) {
        return BasketResponse.builder()
                .productId(
                        basket.getProduct().getId())
                .productName(
                        basket.getProduct().getName())
                .description(
                        basket.getProduct().getDescription())
                .price(
                        basket.getProduct().getPrice())
                .quantity(
                        basket.getQuantity())
                .build();
    }
}
