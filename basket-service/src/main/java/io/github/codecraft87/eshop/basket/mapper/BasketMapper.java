package io.github.codecraft87.eshop.basket.mapper;

import io.github.codecraft87.eshop.basket.dto.BasketResponse;
import io.github.codecraft87.eshop.basket.entity.BasketItem;

public class BasketMapper {
  public static BasketResponse toBasketItemResponse(BasketItem basketItem) {
    return BasketResponse.builder()
        .productId(basketItem.getProductId())
        .productName(basketItem.getProductName())
        .price(basketItem.getUnitPrice())
        .quantity(basketItem.getQuantity())
        .build();
  }
}
