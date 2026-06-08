package io.github.codecraft87.eshop.order.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.codecraft87.eshop.common.dto.OperationResponse;
import io.github.codecraft87.eshop.order.dto.OrderRequest;
import io.github.codecraft87.eshop.order.dto.OrderResponse;
import io.github.codecraft87.eshop.order.service.OrderService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService service) {
        this.orderService = service;
    }

    @GetMapping("/about")
    public ResponseEntity<String> about() {
        return ResponseEntity.ok("<h1>Order Service is running.</h1>");
    }

    @PostMapping
    public ResponseEntity<OperationResponse> createOrder(
                @Valid 
                @RequestBody OrderRequest orderRequest) {
        final Long orderId = orderService.createOrder(orderRequest);
        return ResponseEntity.status(
                HttpStatus.CREATED)
                .body(
                        new OperationResponse(orderId, "Order Created"));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> retrieveOrderDetails(
                                @PathVariable("orderId") Long orderId) {
        return ResponseEntity
                .ok()
                .body(orderService.getOrderDetails(orderId));

    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderResponse>> getOrders(
                            @PathVariable("userId") Long userId) {
        return ResponseEntity
                .ok()
                .body(orderService.getOrders(userId));

    }

    @PutMapping("/{orderId}/cancel")
    public ResponseEntity<OperationResponse> cancelOrder(
                        @PathVariable("orderId") Long orderId) {
        OrderResponse cancelledOrder = orderService.cancelOrder(orderId);
        return ResponseEntity
                .ok()
                .body(
                        new OperationResponse(cancelledOrder.getOrderId(), "Order Cancelled"));
    }

    @PutMapping("/{orderId}")
    public ResponseEntity<OrderRequest> updateOrder(
                    @PathVariable("orderId") Long orderId, 
                    @RequestBody OrderRequest orderDto) {
        OrderRequest updatedOrder = orderService
                .updateOrder(orderId, orderDto);
        System.out.println(updatedOrder);
        return ResponseEntity.ok().body(updatedOrder);
    }
}
