package io.github.codecraft87.eshop.order.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.github.codecraft87.eshop.order.common.constants.ResponsMessageConstants;
import io.github.codecraft87.eshop.order.common.enums.PaymentMode;
import io.github.codecraft87.eshop.order.dto.OperationResponse;
import io.github.codecraft87.eshop.order.dto.OrderRequest;
import io.github.codecraft87.eshop.order.dto.OrderResponse;
import io.github.codecraft87.eshop.order.dto.ProcessOrderInput;
import io.github.codecraft87.eshop.order.security.UserPrincipal;
import io.github.codecraft87.eshop.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/orders/api")
@Slf4j
@RequiredArgsConstructor
public class OrderController {

  private final OrderService orderService;

  @GetMapping("/about")
  public ResponseEntity<String> about() {
    return ResponseEntity.ok("<h1>Order Service is running.</h1>");
  }

  @GetMapping("/{orderId}")
  public ResponseEntity<OperationResponse<OrderResponse>> retrieveOrderDetails(@PathVariable("orderId") Long orderId) {
    log.info("Retrieve order details request received");
    OperationResponse<OrderResponse> response = new OperationResponse<>(orderId,
        ResponsMessageConstants.ORDER_RETRIEVED);
    OrderResponse order = orderService.getOrderDetails(orderId);
    response.setData(order);
    return ResponseEntity.ok().body(response);
  }

  @GetMapping
  public ResponseEntity<OperationResponse<List<OrderResponse>>> getOrder(Authentication authentication) {
    UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
    log.info("Getting basket details for user {}", userPrincipal.getUserId());
    log.info("Get all order for user request received ");
    OperationResponse<List<OrderResponse>> response = new OperationResponse<>(userPrincipal.getUserId(),
        ResponsMessageConstants.ORDER_RETRIEVED);
    List<OrderResponse> allOrders = orderService.getOrders(userPrincipal.getUserId());
    response.setData(allOrders);
    return ResponseEntity.ok().body(response);
  }

  @PutMapping("/{orderId}/cancel")
  public ResponseEntity<OperationResponse<Object>> cancelOrder(@PathVariable("orderId") Long orderId) {
    log.info("Order cancellation request received ");
    OrderResponse cancelledOrder = orderService.cancelOrder(orderId);
    return ResponseEntity.ok()
        .body(
            new OperationResponse<>(
                cancelledOrder.getOrderId(),
                ResponsMessageConstants.ORDER_CANCELLED));
  }

  @PutMapping("/{orderId}")
  public ResponseEntity<OperationResponse<OrderResponse>> updateOrder(
      @PathVariable("orderId") Long orderId, @RequestBody OrderRequest orderDto) {
    log.info("Modification of order request received");
    OrderResponse updatedOrder = orderService.updateOrder(orderId, orderDto);
    OperationResponse<OrderResponse> response = new OperationResponse<>(orderId,
        ResponsMessageConstants.ORDER_UPDATED);
    response.setData(updatedOrder);
    return ResponseEntity.ok().body(response);
  }

  @PostMapping("/{orderId}/process")
  public ResponseEntity<OperationResponse<Object>> processOrder(
      @PathVariable("orderId") Long orderId,
      @RequestParam(defaultValue = "SIMULATED_SUCCESS") PaymentMode paymentMode) {
    log.info("Request for processing order received");
    log.info("Processing order " + orderId);
    orderService.processOrder(new ProcessOrderInput(orderId, paymentMode));
    return ResponseEntity.ok(
        new OperationResponse<>(orderId,
            ResponsMessageConstants.ORDER_PROCSSED));
  }
}
