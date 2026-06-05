package org.orderpaymentsystem.repository;

import org.orderpaymentsystem.common.enums.OrderStatus;
import org.orderpaymentsystem.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByUserIdAndStatus(String userId, OrderStatus status);
}
