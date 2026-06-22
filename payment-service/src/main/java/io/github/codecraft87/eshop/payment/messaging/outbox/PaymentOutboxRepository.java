package io.github.codecraft87.eshop.payment.messaging.outbox;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentOutboxRepository extends JpaRepository<PaymentOutboxMessage, UUID> {
  List<PaymentOutboxMessage> findByStatusInOrderByCreatedAt(List<PaymentEventStatus> statuses);
}
