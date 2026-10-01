package io.github.codecraft87.eshop.basket.service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;

import io.github.codecraft87.eshop.basket.catalog.CatalogClient;
import io.github.codecraft87.eshop.basket.catalog.ProductResponse;
import io.github.codecraft87.eshop.basket.dto.ProductSnapshot;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Retry(name = "catalogService")
@CircuitBreaker(name = "catalogService")
@Slf4j
@RequiredArgsConstructor
@Service
public class CatalogModuleServiceImpl implements CatalogModuleService {

  private final CatalogClient catalogClient;

  @Override
  public CompletionStage<ProductSnapshot> getProductById(Long productId) {
    try {
      log.info("Fetching product by ID: {}", productId);
      return CompletableFuture.supplyAsync(() -> {
        ProductResponse productResponse = catalogClient.getProductById(productId);

        return new ProductSnapshot(
            productResponse.getId(),
            productResponse.getName(),
            productResponse.getPrice());
      });
    } catch (IllegalStateException e) {
      log.error("Error occurred while fetching product by ID: " + productId, e);
      throw new ResourceAccessException("Catalog service is unavailable. Please try again later.");
    }
  }
}
