package org.orderpaymentsystem.repository;

import java.util.List;

import org.orderpaymentsystem.common.enums.PaymentStatus;
import org.orderpaymentsystem.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    boolean existsByOrderIdAndStatus(Long orderId, PaymentStatus status);

    List<Payment> findByOrderId(Long orderId);
}
