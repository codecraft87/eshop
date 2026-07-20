package io.github.codecraft87.eshop.payment.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import io.github.codecraft87.eshop.payment.common.enums.PaymentStatus;
import io.github.codecraft87.eshop.payment.entity.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

  boolean existsByOrderIdAndStatus(Long orderId, PaymentStatus status);

  List<Payment> findByOrderId(Long orderId);
}
