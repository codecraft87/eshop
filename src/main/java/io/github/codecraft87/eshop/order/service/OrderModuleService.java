package io.github.codecraft87.eshop.order.service;

import io.github.codecraft87.eshop.order.dto.OrderRequest;
import io.github.codecraft87.eshop.order.entity.Order;

public interface OrderModuleService {

    Long createOrder(OrderRequest orderRequest);
    
    Order getOrder(Long orderId);
    
    Order saveOrder(Order order);
}
