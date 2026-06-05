package org.orderpaymentsystem.dto;

import org.orderpaymentsystem.entity.BasketItem;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BasketItemResponse {

    private Long productId;
    
    private String productName;
    
    private String description;
    
    private Double price;
    
    private Integer quantity;
    
    public static BasketItemResponse getBasketItemResponse(
        BasketItem basketItem) {
        return BasketItemResponse.builder()
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
