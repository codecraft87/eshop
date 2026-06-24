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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.github.codecraft87.eshop.order.dto.OperationResponse;
import io.github.codecraft87.eshop.order.dto.OrderRequest;
import io.github.codecraft87.eshop.order.dto.OrderResponse;
import io.github.codecraft87.eshop.order.dto.ProcessOrderInput;
import io.github.codecraft87.eshop.order.enums.PaymentMode;
import io.github.codecraft87.eshop.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/orders")
@Slf4j
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
  public ResponseEntity<io.github.codecraft87.eshop.order.dto.OperationResponse> createOrder(
      @Valid @RequestBody OrderRequest orderRequest) {
    final Long orderId = orderService.createOrder(orderRequest);
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(new OperationResponse(orderId, "Order Created"));
  }

  @GetMapping("/{orderId}")
  public ResponseEntity<OrderResponse> retrieveOrderDetails(@PathVariable("orderId") Long orderId) {
    return ResponseEntity.ok().body(orderService.getOrderDetails(orderId));
  }

  @GetMapping("/user/{userId}")
  public ResponseEntity<List<OrderResponse>> getOrders(@PathVariable("userId") Long userId) {
    return ResponseEntity.ok().body(orderService.getOrders(userId));
  }

  @PutMapping("/{orderId}/cancel")
  public ResponseEntity<OperationResponse> cancelOrder(@PathVariable("orderId") Long orderId) {
    OrderResponse cancelledOrder = orderService.cancelOrder(orderId);
    return ResponseEntity.ok()
        .body(new OperationResponse(cancelledOrder.getOrderId(), "Order Cancelled"));
  }

  @PutMapping("/{orderId}")
  public ResponseEntity<OrderRequest> updateOrder(
      @PathVariable("orderId") Long orderId, @RequestBody OrderRequest orderDto) {
    OrderRequest updatedOrder = orderService.updateOrder(orderId, orderDto);
    return ResponseEntity.ok().body(updatedOrder);
  }

  @PostMapping("/{orderId}/process")
  public ResponseEntity<String> processOrder(
      @PathVariable("orderId") Long orderId,
      @RequestParam(defaultValue = "SIMULATED_SUCCESS") PaymentMode paymentMode) {

    log.info("Processing order " + orderId);
    orderService.processOrder(new ProcessOrderInput(orderId, paymentMode));
    return ResponseEntity.ok("Order procssed");
  }
}
