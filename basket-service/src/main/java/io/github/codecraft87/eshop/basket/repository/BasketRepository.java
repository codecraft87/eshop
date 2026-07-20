package io.github.codecraft87.eshop.basket.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import io.github.codecraft87.eshop.basket.common.enums.BasketStatus;
import io.github.codecraft87.eshop.basket.entity.Basket;

public interface BasketRepository extends JpaRepository<Basket, Long> {
  List<Basket> findByUserIdAndStatus(Long orderId, BasketStatus status);
}
