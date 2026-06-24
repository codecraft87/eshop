package io.github.codecraft87.eshop.order.messaging.idempotency;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderProcessedEventRepository extends JpaRepository<OrderProcessedEvent, UUID> {}
