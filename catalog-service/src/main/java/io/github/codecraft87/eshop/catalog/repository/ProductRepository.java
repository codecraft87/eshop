package io.github.codecraft87.eshop.catalog.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import io.github.codecraft87.eshop.catalog.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {}
