package io.github.codecraft87.eshop.basket.service;

import java.util.concurrent.CompletionStage;

import io.github.codecraft87.eshop.basket.dto.ProductSnapshot;

public interface CatalogModuleService {

  CompletionStage<ProductSnapshot> getProductById(Long productId);

}
