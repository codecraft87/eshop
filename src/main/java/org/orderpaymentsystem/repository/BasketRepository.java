package org.orderpaymentsystem.repository;

import org.orderpaymentsystem.common.enums.BasketStatus;
import org.orderpaymentsystem.entity.Basket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BasketRepository extends JpaRepository<Basket, Long> {
    Basket findByUserIdAndStatus(Long orderId, BasketStatus status);
    }
