package org.orderpaymentsystem.repository;

import org.orderpaymentsystem.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

}
