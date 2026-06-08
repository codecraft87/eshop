package io.github.codecraft87.eshop.catalog.service;

import io.github.codecraft87.eshop.catalog.entity.Product;

public interface CatalogModuleService {

    Product getProduct(Long productId);
}
