package io.github.codecraft87.eshop.catalog.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.codecraft87.eshop.catalog.common.constants.ResponseMessageConstant;
import io.github.codecraft87.eshop.catalog.dto.ProductRequest;
import io.github.codecraft87.eshop.catalog.dto.ProductResponse;
import io.github.codecraft87.eshop.catalog.service.ProductService;
import io.github.codecraft87.eshop.common.OperationResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/products/api")
@RequiredArgsConstructor
public class ProductController {

  private final ProductService productService;

  @GetMapping("/about")
  public ResponseEntity<String> about() {
    return ResponseEntity.ok().body("<h1>Product Service is running.</h1>");
  }

  @PostMapping
  public ResponseEntity<OperationResponse<Object>> addProduct(@RequestBody ProductRequest product) {
    log.info("Add product request received.");
    Long productId = productService.addProduct(product);
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(
            new OperationResponse<>(
                productId,
                ResponseMessageConstant.PRODUCT_CREATED));
  }

  @GetMapping
  public ResponseEntity<OperationResponse<List<ProductResponse>>> getProducts() {
    log.info("Get all products request received.");
    List<ProductResponse> productList = productService.getAllProducts();
    OperationResponse<List<ProductResponse>> response = new OperationResponse<>(null,
        ResponseMessageConstant.PRODUCT_DETAILS);
    response.setData(productList);
    return ResponseEntity.ok().body(response);
  }

  @GetMapping("/{productId}")
  public ResponseEntity<OperationResponse<ProductResponse>> getProduct(@PathVariable("productId") Long pid) {
    log.info("Get product request received {}.", pid);
    ProductResponse product = productService.getProductDetails(pid);
    OperationResponse<ProductResponse> response = new OperationResponse<>(null,
        ResponseMessageConstant.PRODUCT_DETAILS);
    response.setData(product);
    return ResponseEntity.ok().body(response);
  }

  @PutMapping("/{productId}")
  public ResponseEntity<OperationResponse<ProductResponse>> updateProduct(
      @PathVariable("productId") Long pid, @RequestBody ProductRequest productDto) {
    log.info("Product modication request received.");
    ProductResponse updatedProduct = productService.updateProduct(pid, productDto);
    OperationResponse<ProductResponse> response = new OperationResponse<>(pid,
        ResponseMessageConstant.PRODUCT_UPDATED);
    response.setData(updatedProduct);
    return ResponseEntity.ok().body(response);
  }

  @DeleteMapping("/{productId}")
  public ResponseEntity<OperationResponse<Object>> deleteProduct(@PathVariable Long pid) {
    log.info("Product deletion request received.");
    Long productId = productService.deleteProduct(pid);
    return ResponseEntity.ok().body(
        new OperationResponse<>(
            productId,
            ResponseMessageConstant.PRODUCT_DELETED));
  }
}
