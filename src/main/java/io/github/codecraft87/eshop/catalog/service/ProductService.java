package io.github.codecraft87.eshop.catalog.service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import io.github.codecraft87.eshop.catalog.dto.ProductRequest;
import io.github.codecraft87.eshop.catalog.entity.Product;
import io.github.codecraft87.eshop.catalog.mapper.ProductMapper;
import io.github.codecraft87.eshop.catalog.repository.ProductRepository;
import io.github.codecraft87.eshop.exceptions.ProductNotFoundException;
import io.github.codecraft87.eshop.notification.service.NotificationService;
import jakarta.transaction.Transactional;

@Service
public class ProductService {

    private final NotificationService notificationService;
    private ProductRepository repo;
    
    public ProductService(ProductRepository productRepository, NotificationService notificationService) {
        this.repo = productRepository;
        this.notificationService = notificationService;
    }
    
    @Transactional
    public Long addProduct(ProductRequest productRequest) {
        final Product product = ProductMapper
                                    .getProductEntity(productRequest);
        product.setCreatedAt(Instant.now());
        Long productId = saveProduct(product).getId();

        notificationService.productCreated(productId);
        return productId;
    }

    public Product getProductDetails(Long productId) {
        final Product product = getProductById(productId); 
        // define product response
        return product;
    }

    public List<ProductRequest> getAllProducts(){
        final List<Product> productList = repo.findAll();
        final List<ProductRequest> productDtoList = 
                productList
                    .stream()
                    .map(ProductMapper::getProductRequest)
                    .collect(Collectors.toList());
        return productDtoList;
    }
    
    @Transactional
    public ProductRequest updateProduct(ProductRequest productDto) {
        final Product productToUpdate = getProductById(productDto.getId());
        productToUpdate.setUpdatedAt(Instant.now());
        productToUpdate.setName(productDto.getName());
        productToUpdate.setDescription(productDto.getDescription());
        productToUpdate.setPrice(productDto.getPrice());
        
        final Product updatedProduct = saveProduct(productToUpdate);
        notificationService.productUpdated(updatedProduct.getId());
        return ProductRequest.getProductDTO(updatedProduct);
    }
    
    @Transactional
    public Long deleteProduct(Long productId) {
        final Product productToBeDeleted = getProductById(productId);
        repo.delete(productToBeDeleted);

        notificationService.productDeleted(productId);
        
        return productId;
    }

    private Product getProductById(Long productId) {
        final Product product = repo
                    .findById(productId)
                    .orElseThrow(()-> 
                            new ProductNotFoundException(productId));
        return product;
    }
    
    private Product saveProduct(Product product) {
        product.setUpdatedAt(Instant.now());
        return repo.save(product);
    }
}
