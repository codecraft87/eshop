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

import io.github.codecraft87.eshop.order.dto.OrderDTO;
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
    public ResponseEntity<OrderResponse> createOrder(@Valid @RequestBody OrderDTO orderDto) {
        final Long orderId = orderService.createOrder(orderDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(new OrderResponse(orderId, "Order Created"));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDTO> retrieveOrderDetails(@PathVariable("orderId") Long orderId) {
        return ResponseEntity.ok().body(orderService.getOrderDetails(orderId));

    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderDTO>> getOrders(@PathVariable("userId") Long userId) {
        return ResponseEntity.ok().body(orderService.getOrders(userId));

    }

    @PutMapping("/{orderId}/cancel")
    public ResponseEntity<OrderResponse> cancelOrder(@PathVariable("orderId") Long orderId) {
        OrderDTO cancelledOrder = orderService.cancelOrder(orderId);
        return ResponseEntity.ok().body(new OrderResponse(cancelledOrder.getOrderId(), "Order Cancelled"));
    }

    @PutMapping("/{orderId}")
    public ResponseEntity<OrderDTO> updateOrder(@PathVariable("orderId") Long orderId, @RequestBody OrderDTO orderDto) {
        System.out.println("updating order "+orderId + "DT "+orderDto);
        orderDto.setOrderId(orderId);
        OrderDTO updatedOrder = orderService.updateOrder(orderDto);
        System.out.println(updatedOrder);
        return ResponseEntity.ok().body(updatedOrder);
    }
}
