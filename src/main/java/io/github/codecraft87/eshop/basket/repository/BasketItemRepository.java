package io.github.codecraft87.eshop.basket.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.github.codecraft87.eshop.basket.entity.BasketItem;

@Repository
public interface BasketItemRepository extends JpaRepository<BasketItem, Long> {

}
