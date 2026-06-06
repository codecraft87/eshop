package io.github.codecraft87.eshop.basket.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BasketRequest {

    public BasketRequest(Long userId) {
        this.userId = userId;
    }
    
    private Long userId;
    
    private List<BasketItemRequest> items;
}
