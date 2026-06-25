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

import io.github.codecraft87.eshop.catalog.dto.ProductRequest;
import io.github.codecraft87.eshop.catalog.dto.ProductResponse;
import io.github.codecraft87.eshop.catalog.service.ProductService;
import io.github.codecraft87.eshop.common.dto.OperationResponse;

@RestController
@RequestMapping("/products")
public class ProductController {

  private ProductService productService;

  public ProductController(ProductService service) {
    this.productService = service;
  }

  @GetMapping("/about")
  public ResponseEntity<String> about() {
    return ResponseEntity.ok().body("<h1>Product Service is running.</h1>");
  }

  @PostMapping
  public ResponseEntity<OperationResponse> addProduct(@RequestBody ProductRequest product) {
    Long productId = productService.addProduct(product);
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(new OperationResponse(productId, "Product Created"));
  }

  @GetMapping
  public ResponseEntity<List<ProductResponse>> getProducts() {
    List<ProductResponse> productList = productService.getAllProducts();
    return ResponseEntity.ok().body(productList);
  }

  @GetMapping("/{productId}")
  public ResponseEntity<ProductResponse> getProduct(@PathVariable("productId") Long pid) {
    return ResponseEntity.ok().body(productService.getProductDetails(pid));
  }

  @PutMapping("/{productId}")
  public ResponseEntity<ProductRequest> updateProduct(
      @PathVariable("productId") Long pid, @RequestBody ProductRequest productDto) {
    ProductRequest updatedProduct = productService.updateProduct(pid, productDto);
    return ResponseEntity.ok().body(updatedProduct);
  }

  @DeleteMapping("/{productId}")
  public ResponseEntity<OperationResponse> deleteProduct(@PathVariable Long pid) {
    Long productId = productService.deleteProduct(pid);
    return ResponseEntity.ok().body(new OperationResponse(productId, "Product deleted"));
  }
}
