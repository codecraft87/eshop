package io.github.codecraft87.eshop.order.dto;

import java.util.ArrayList;
import java.util.List;

import io.github.codecraft87.eshop.order.enums.OrderStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class OrderRequest {

  @NotNull(message = "{user.notnull}")
  private String userId;

  @NotNull(message = "{amount.notnull}")
  private Double totalAmount;

  private OrderStatus status;

  @Builder.Default private List<OrderItemRequest> orderItems = new ArrayList<>();
}
