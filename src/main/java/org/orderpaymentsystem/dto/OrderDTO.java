package org.orderpaymentsystem.dto;

import java.time.Instant;

import org.orderpaymentsystem.common.enums.OrderStatus;
import org.orderpaymentsystem.entity.Order;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrderDTO {

    @NotNull(message = "{user.notnull}")
    private String userId;

    @NotNull(message = "{amount.notnull}")
    private Double amount;

    private Long orderId;
    private Instant createdAt;
    private Instant updatedAt;
    private OrderStatus status;

    public void set(Order order) {
        this.userId = order.getUserId();
        this.amount = order.getAmount();
        this.orderId = order.getId();
        this.status = order.getStatus();
        this.createdAt = order.getCreatedAt();
        this.updatedAt = order.getUpdatedAt();
    }

    public static OrderDTO getOrderDTO(Order order) {
        OrderDTO dto = new OrderDTO();
        dto.set(order);
        return dto;
    }
}
