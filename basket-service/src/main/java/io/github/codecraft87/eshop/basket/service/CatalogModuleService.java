package io.github.codecraft87.eshop.basket.service;

import io.github.codecraft87.eshop.basket.dto.ProductSnapshot;

public interface CatalogModuleService {

  ProductSnapshot getProduct(Long productId);
}
