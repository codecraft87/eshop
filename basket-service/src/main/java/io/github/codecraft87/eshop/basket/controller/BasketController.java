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

import io.github.codecraft87.eshop.basket.dto.BasketRequest;
import io.github.codecraft87.eshop.basket.dto.BasketResponse;
import io.github.codecraft87.eshop.basket.dto.OperationResponse;
import io.github.codecraft87.eshop.basket.service.BasketService;

@RestController
@RequestMapping("/basket")
public class BasketController {

  private BasketService basketService;

  public BasketController(BasketService service) {
    this.basketService = service;
  }

  @PutMapping
  public ResponseEntity<OperationResponse> modifyBasket(@RequestBody BasketRequest itemRequest) {
    long basketId = basketService.saveBasket(itemRequest);
    return ResponseEntity.ok().body(new OperationResponse(basketId, "Basket Updated"));
  }

  @GetMapping("/{userId}")
  public ResponseEntity<List<BasketResponse>> getBasket(@PathVariable Long userId) {
    List<BasketResponse> basketResponse = basketService.getBasketDetails(userId);
    return ResponseEntity.ok().body(basketResponse);
  }

  @PostMapping("/checkout")
  public ResponseEntity<String> checkout(@RequestBody BasketRequest itemRequest) {

    basketService.checkout(itemRequest);

    return ResponseEntity.ok().body("Cart process");
  }
}
