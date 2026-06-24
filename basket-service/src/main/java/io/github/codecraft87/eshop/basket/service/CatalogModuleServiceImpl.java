package io.github.codecraft87.eshop.basket.service;

import org.springframework.stereotype.Service;

import io.github.codecraft87.eshop.basket.dto.ProductSnapshot;

@Service
public class CatalogModuleServiceImpl implements CatalogModuleService {

  @Override
  public ProductSnapshot getProduct(Long productId) {
      return new ProductSnapshot(
              productId,
              "TEST_PRODUCT_" + productId,
              100.0);
  }
}
