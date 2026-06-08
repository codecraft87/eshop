package io.github.codecraft87.eshop.order.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class OrderItemRequest {
    
    private long productId;

    private String productName;

    private Integer quantity;

    private Double price;
}
