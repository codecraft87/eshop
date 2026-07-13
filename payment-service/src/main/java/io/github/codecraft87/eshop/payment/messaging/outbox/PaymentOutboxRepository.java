package io.github.codecraft87.eshop.payment.messaging.outbox;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentOutboxRepository extends JpaRepository<PaymentOutboxMessage, Long> {
  List<PaymentOutboxMessage> findByStatusInOrderByCreatedAt(List<PaymentEventStatus> statuses);
}
