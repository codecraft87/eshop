package org.orderpaymentsystem.controller;

import java.util.List;

import org.orderpaymentsystem.dto.BasketItemRequest;
import org.orderpaymentsystem.dto.BasketItemResponse;
import org.orderpaymentsystem.service.BasketService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/basket")
public class BasketController {

    private BasketService basketService;

    public BasketController(BasketService service){
        this.basketService = service;
    }

    @PutMapping
    public ResponseEntity<BasketResponse> modifyBasket(@RequestBody BasketItemRequest itemRequest) {
        long basketId = basketService.saveBasket(itemRequest);
        return ResponseEntity.ok().body(new BasketResponse(basketId, "Basket Updated"));
    }
    
    @GetMapping("/{userId}")
    public ResponseEntity<List<BasketItemResponse>> getBasket(@PathVariable Long userId) {
        List<BasketItemResponse> basketResponse = basketService.getBasketDetails(userId);
        return ResponseEntity.ok().body(basketResponse);
    }

    @PostMapping("/checkout")
    public ResponseEntity<String> checkout(@RequestBody BasketItemRequest itemRequest) {
         basketService.checkout(itemRequest);
        return ResponseEntity.ok().body("Cart process");
    }
}
