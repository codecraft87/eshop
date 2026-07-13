package io.github.codecraft87.eshop.basket.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import io.github.codecraft87.eshop.basket.entity.BasketItem;

public interface BasketItemRepository extends JpaRepository<BasketItem, Long> {
}
