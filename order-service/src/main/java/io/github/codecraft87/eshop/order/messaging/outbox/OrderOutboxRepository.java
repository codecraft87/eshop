package io.github.codecraft87.eshop.order.messaging.outbox;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderOutboxRepository extends JpaRepository<OrderOutboxMessage, Long> {
  List<OrderOutboxMessage> findByStatusInOrderByCreatedAt(List<OrderEventStatus> statuses);
}
