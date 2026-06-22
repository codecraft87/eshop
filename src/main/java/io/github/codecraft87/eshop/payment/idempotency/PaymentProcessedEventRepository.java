package io.github.codecraft87.eshop.payment.idempotency;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentProcessedEventRepository
    extends JpaRepository<PaymentProcessedEvent, UUID> {}
