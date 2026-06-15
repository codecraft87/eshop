package io.github.codecraft87.eshop.basket.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.github.codecraft87.eshop.basket.entity.BasketOutboxEvent;
import io.github.codecraft87.eshop.basket.enums.BasketEventStatus;

@Repository
public interface BasketOutboxEventRepository extends JpaRepository<BasketOutboxEvent, Long> {
    List<BasketOutboxEvent> findByStatusInOrderByCreatedAt(
        List<BasketEventStatus> statuses);
}
