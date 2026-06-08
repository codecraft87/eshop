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
@Builder
public class BasketResponse {

    private Long productId;
    
    private String productName;
    
    private String description;
    
    private Double price;
    
    private Integer quantity;
}
