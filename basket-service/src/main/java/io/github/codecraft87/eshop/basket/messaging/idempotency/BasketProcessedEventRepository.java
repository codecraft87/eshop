package io.github.codecraft87.eshop.basket.messaging.idempotency;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BasketProcessedEventRepository extends JpaRepository<BasketProcessedEvent, UUID> {}
