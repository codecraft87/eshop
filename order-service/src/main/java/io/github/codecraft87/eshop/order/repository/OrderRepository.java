package io.github.codecraft87.eshop.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import io.github.codecraft87.eshop.order.entity.Order;
import io.github.codecraft87.eshop.order.enums.OrderStatus;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

  List<Order> findByUserIdAndStatus(String userId, OrderStatus status);
}
