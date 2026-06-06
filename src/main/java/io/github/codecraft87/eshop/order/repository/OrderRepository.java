package io.github.codecraft87.eshop.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.github.codecraft87.eshop.common.enums.OrderStatus;
import io.github.codecraft87.eshop.order.entity.Order;

import java.util.List;


@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByUserIdAndStatus(String userId, OrderStatus status);
}
