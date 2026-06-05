package org.orderpaymentsystem.dto;

import org.orderpaymentsystem.entity.OrderItem;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemDTO {

    private long productId;

    private String productName;

    private Integer quantity;

    private Double price;

    public static OrderItemDTO getOrderItemDTO(
        OrderItem orderItem){
        return OrderItemDTO.builder()
                .productId(
                    orderItem.getProductId() 
                )
                .productName(
                    orderItem.getProductName()
                )
                .quantity(
                    orderItem.getQuantity()
                )
                .price(
                    orderItem.getPrice()
                )
                .build();
    }

    public static OrderItem getOrderItemEntity(
        OrderItemDTO orderItemDTO){
             return OrderItem.builder()
        .productId(
            orderItemDTO.getProductId()
        )
        .productName(
            orderItemDTO.getProductName()
        )
        .quantity(
            orderItemDTO.getQuantity()
        )
        .price(
            orderItemDTO.getPrice()
        )
        .build();
        }
}
