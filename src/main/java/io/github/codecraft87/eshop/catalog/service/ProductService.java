package io.github.codecraft87.eshop.catalog.service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import io.github.codecraft87.eshop.catalog.dto.ProductRequest;
import io.github.codecraft87.eshop.catalog.dto.ProductResponse;
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

    public ProductResponse getProductDetails(Long productId) {
        final Product product = findProductById(productId); 
        
        return ProductMapper.getProductResponse(product);
    }

    public List<ProductResponse> getAllProducts(){
        final List<Product> productList = repo.findAll();
        final List<ProductResponse> productDtoList = 
                productList
                    .stream()
                    .map(ProductMapper::getProductResponse)
                    .collect(Collectors.toList());
        return productDtoList;
    }
    
    @Transactional
    public ProductResponse updateProduct(Long productId,
            ProductRequest productRequest) {
        final Product productToUpdate = findProductById(productId);
        productToUpdate.setUpdatedAt(Instant.now());
        productToUpdate.setName(productRequest.getName());
        productToUpdate.setDescription(productRequest.getDescription());
        productToUpdate.setPrice(productRequest.getPrice());
        
        final Product updatedProduct = saveProduct(productToUpdate);
        notificationService.productUpdated(updatedProduct.getId());
        return ProductMapper.getProductResponse(updatedProduct);
    }
    
    @Transactional
    public Long deleteProduct(Long productId) {
        final Product productToBeDeleted = findProductById(productId);
        repo.delete(productToBeDeleted);

        notificationService.productDeleted(productId);
        
        return productId;
    }

    public Product findProductById(Long productId) {
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
