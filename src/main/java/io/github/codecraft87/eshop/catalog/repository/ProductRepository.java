package io.github.codecraft87.eshop.catalog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.github.codecraft87.eshop.catalog.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

}
