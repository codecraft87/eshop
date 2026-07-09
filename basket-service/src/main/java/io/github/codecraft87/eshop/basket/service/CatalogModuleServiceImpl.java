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
    ProductResponse productResponse = catalogClient.getProductById(productId);
    return new ProductSnapshot(
        productResponse.getId(), productResponse.getName(), productResponse.getPrice());
  }
}
