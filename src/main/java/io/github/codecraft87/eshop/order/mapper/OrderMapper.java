package io.github.codecraft87.eshop.order.mapper;

import java.util.List;

import io.github.codecraft87.eshop.order.dto.OrderItemRequest;
import io.github.codecraft87.eshop.order.dto.OrderRequest;
import io.github.codecraft87.eshop.order.dto.OrderResponse;
import io.github.codecraft87.eshop.order.entity.Order;
import io.github.codecraft87.eshop.order.entity.OrderItem;

public class OrderMapper {
    public static OrderResponse getOrderResponse(
        Order order){
        return OrderResponse.builder()
        .orderId(
                order.getId()
                )
        .userId(
            order.getUserId()
        )
        .totalAmount(
            order.getTotalAmount()
        )
        .status(
            order.getStatus()
        )
        .orderItems(
            order.getOrderItems()
                .stream()
                .map(OrderMapper::getOrderItemRequest)
                .toList()
        )
        .createdAt(
                order.getCreatedAt()
                )
        .updatedAt(
                order.getUpdatedAt()
                )
        .build();
}

    public static Order getOrderEntity(OrderRequest orderRequest){
     Order order =  Order.builder()
                .totalAmount(
                     orderRequest.getTotalAmount()
                )
                .userId(
                     orderRequest.getUserId()
                )
                .build();
        List<OrderItem> items = orderRequest.getOrderItems()
            .stream()
            .map(OrderMapper::getOrderItemEntity)
            .toList();
        items.forEach(item ->
            item.setOrder(order));
        order.setOrderItems(items);
        return order;
    }
    
    public static OrderItemRequest getOrderItemRequest(
        OrderItem orderItem){
        return OrderItemRequest.builder()
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
        OrderItemRequest orderItemDTO){
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
