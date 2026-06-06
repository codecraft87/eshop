package io.github.codecraft87.eshop.basket.dto;

import io.github.codecraft87.eshop.basket.entity.BasketItem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BasketResponse {

    private Long productId;
    
    private String productName;
    
    private String description;
    
    private Double price;
    
    private Integer quantity;
    
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
