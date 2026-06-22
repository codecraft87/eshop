package io.github.codecraft87.eshop.order.dto;

import java.time.Instant;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Setter
@Getter
@SuperBuilder
public class OrderResponse extends OrderRequest {
  private Long orderId;
  private Instant createdAt;
  private Instant updatedAt;
}
