package io.github.codecraft87.eshop.basket.catalog;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange("http://localhost:8081/products")
public interface CatalogClient {
    
    @GetExchange("/{productId}")
    public ProductResponse getProductById(@PathVariable Long productId);
}
