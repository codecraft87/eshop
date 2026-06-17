package io.github.codecraft87.eshop.basket.idempotency;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProcessedEventRepository extends JpaRepository<ProcessedEvent, UUID> {

}
