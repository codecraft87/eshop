package io.github.codecraft87.eshop.basket.service;

import org.springframework.stereotype.Service;

import io.github.codecraft87.eshop.basket.catalog.CatalogClient;
import io.github.codecraft87.eshop.basket.catalog.ProductResponse;
import io.github.codecraft87.eshop.basket.dto.ProductSnapshot;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class CatalogModuleServiceImpl implements CatalogModuleService {

  private final CatalogClient catalogClient;
    
  @Override 
  public ProductSnapshot getProductById(Long productId) {
      log.info("Getting product by id {}", productId);
      try {
          ProductResponse product = catalogClient.getProductById(productId);
          return new ProductSnapshot(product.getId(), product.getName(), product.getPrice());
      }catch (Exception e) {
          log.info("Error {} ", e.getMessage());
          log.error("Error trace", e);
      }
      return null;
  }
}
