package io.github.codecraft87.eshop.basket.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.codecraft87.eshop.basket.common.constants.ResponseMessageConstant;
import io.github.codecraft87.eshop.basket.dto.BasketRequest;
import io.github.codecraft87.eshop.basket.dto.BasketResponse;
import io.github.codecraft87.eshop.basket.dto.OperationResponse;
import io.github.codecraft87.eshop.basket.service.BasketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/basket")
@RequiredArgsConstructor
public class BasketController {

  private final BasketService basketService;

  @PutMapping
  public ResponseEntity<OperationResponse<Object>> modifyBasket(
      @RequestBody BasketRequest itemRequest) {
    log.info("Modify basket request recieved for user {}", itemRequest.getUserId());
    long basketId = basketService.saveBasket(itemRequest);
    return ResponseEntity.ok().body(
        new OperationResponse<>(
            basketId,
            ResponseMessageConstant.BASKET_UPDATED));
  }

  @GetMapping("/user/{userId}")
  public ResponseEntity<OperationResponse<List<BasketResponse>>> getBasket(@PathVariable Long userId) {
    log.info("Getting basket details for user {}", userId);
    List<BasketResponse> basketResponse = basketService.getBasketDetails(userId);
    OperationResponse<List<BasketResponse>> response = new OperationResponse<>(
        userId, ResponseMessageConstant.BASKET_DETAILS);
    response.setData(basketResponse);
    return ResponseEntity.ok().body(response);
  }

  @PostMapping("/checkout")
  public ResponseEntity<OperationResponse<Object>> checkout(@RequestBody BasketRequest itemRequest) {
    log.info("Checkout request received for basket for user {}", itemRequest.getUserId());
    Long basketId = basketService.checkout(itemRequest);
    return ResponseEntity.ok().body(
        new OperationResponse<>(
            basketId,
            ResponseMessageConstant.BASKET_CHECKED_OUT));
  }
}
