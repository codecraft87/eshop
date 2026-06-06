package io.github.codecraft87.eshop.catalog.mapper;

import io.github.codecraft87.eshop.catalog.dto.ProductRequest;
import io.github.codecraft87.eshop.catalog.entity.Product;

public class ProductMapper {

    public static Product getProductEntity(ProductRequest product) {
        return Product.builder()
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .build();
    }
    
    public static ProductRequest getProductRequest(Product product) {
        return ProductRequest.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .build();
    }
}
