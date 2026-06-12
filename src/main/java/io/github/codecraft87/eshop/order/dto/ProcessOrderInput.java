package io.github.codecraft87.eshop.order.dto;

public record ProcessOrderInput(
                        Long orderId, 
                        Boolean simulateSuccess) {
}
