package io.github.codecraft87.eshop.catalog.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.codecraft87.eshop.catalog.dto.ProductResponse;
import io.github.codecraft87.eshop.catalog.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/internal/products")
@RequiredArgsConstructor
public class ProductInternalController {

    private final ProductService productService;

    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponse> getProduct(@PathVariable("productId") Long pid) {
        log.info("Internal get product request received {}.", pid);
        ProductResponse product = productService.getProductDetails(pid);
        return ResponseEntity.ok().body(product);
    }
}
