package io.github.codecraft87.eshop.basket.messaging.outbox;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BasketOutboxRepository extends JpaRepository<BasketOutboxMessage, Long> {
  List<BasketOutboxMessage> findByStatusInOrderByCreatedAt(List<BasketOutboxEventStatus> statuses);
}
