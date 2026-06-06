package io.github.codecraft87.eshop.basket.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BasketItemRequest {

    private Long productId;
    
    private Integer quantity;
}
