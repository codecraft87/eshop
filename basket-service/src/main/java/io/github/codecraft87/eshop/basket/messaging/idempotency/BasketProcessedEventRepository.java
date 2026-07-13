package io.github.codecraft87.eshop.basket.messaging.idempotency;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BasketProcessedEventRepository extends JpaRepository<BasketProcessedEvent, UUID> {
}
