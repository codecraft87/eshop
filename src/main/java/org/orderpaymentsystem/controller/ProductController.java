package org.orderpaymentsystem.controller;

import java.util.List;

import org.orderpaymentsystem.dto.ProductDTO;
import org.orderpaymentsystem.entity.Product;
import org.orderpaymentsystem.service.ProductService;
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

@RestController
@RequestMapping("/products")
public class ProductController {

    private ProductService productService;
    
    public ProductController(ProductService service) {
        this.productService = service;
    }
    
    @GetMapping
    public ResponseEntity<String> about() {
        return ResponseEntity.ok().body("<h1>Product Service is running.</h1>");
    }
    
    @PostMapping
    public ResponseEntity<ProductResponse> addProduct(@RequestBody ProductDTO product) {
        Long productId = productService.addProduct(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ProductResponse(productId, "Product Created"));
    }
    
    @GetMapping
    public ResponseEntity<List<ProductDTO>> getProducts() {
        List<ProductDTO> productList = productService.getAllProducts();
        return ResponseEntity.ok().body(productList);
    }
    
    @GetMapping
    @RequestMapping("/{productId}")
    public ResponseEntity<Product> getProduct(@PathVariable("productId") Long pid) {
        return ResponseEntity.ok().body(productService.getProductDetails(pid));
    }
    
    @PutMapping
    @RequestMapping("/{productId}")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable("productId") Long pid,
        @RequestBody ProductDTO productDto) {
        productDto.setId(pid);
        ProductDTO updatedProduct = productService.updateProduct(productDto);
        return ResponseEntity.ok().body(updatedProduct);
    }
    
    @DeleteMapping
    @RequestMapping("/{productId}")
    public ResponseEntity<ProductResponse> deleteProduct(@PathVariable Long pid) {
        Long productId = productService.deleteProduct(pid);
        return ResponseEntity.ok().body(new ProductResponse(productId, "Product deleted"));        
    }
}
