package io.github.codecraft87.eshop.basket.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import io.github.codecraft87.eshop.basket.entity.Basket;
import io.github.codecraft87.eshop.basket.enums.BasketStatus;

public interface BasketRepository extends JpaRepository<Basket, Long> {
  Basket findByUserIdAndStatus(Long orderId, BasketStatus status);
}
