package org.orderpaymentsystem.dto;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.orderpaymentsystem.common.enums.OrderStatus;
import org.orderpaymentsystem.entity.Order;
import org.orderpaymentsystem.entity.OrderItem;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDTO {

    @NotNull(message = "{user.notnull}")
    private String userId;

    @NotNull(message = "{amount.notnull}")
    private Double totalAmount;

    private Long orderId;
    private Instant createdAt;
    private Instant updatedAt;
    private OrderStatus status;

    @Builder.Default
    private List<OrderItemDTO> orderItems = new ArrayList<>();

    public static OrderDTO getOrderDTO(
            Order order){
            return OrderDTO.builder()
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
                    .map(OrderItemDTO::getOrderItemDTO)
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

    public static Order getOrderEntity(OrderDTO orderDTO){
         Order order =  Order.builder()
                    .totalAmount(
                        orderDTO.getTotalAmount()
                    )
                    .userId(
                        orderDTO.getUserId()
                    )
                    .build();
        List<OrderItem> items = orderDTO.getOrderItems()
            .stream()
            .map(OrderItemDTO::getOrderItemEntity)
            .toList();
        items.forEach(item ->
            item.setOrder(order));
        order.setOrderItems(items);
        return order;
    }
}
