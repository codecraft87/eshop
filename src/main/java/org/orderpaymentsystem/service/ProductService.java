package org.orderpaymentsystem.service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import org.orderpaymentsystem.dto.ProductDTO;
import org.orderpaymentsystem.entity.Product;
import org.orderpaymentsystem.exceptions.ProductNotFoundException;
import org.orderpaymentsystem.repository.ProductRepository;

import jakarta.transaction.Transactional;

public class ProductService {

    private ProductRepository repo;
    
    public ProductService(ProductRepository productRepository) {
        this.repo = productRepository;
    }
    
    @Transactional
    public Long addProduct(ProductDTO dto) {
        final Product product = Product.getProductEntity(dto);
        return saveProduct(product).getId();
    }

    public Product getProductDetails(Long productId) {
        final Product product = getProductById(productId); 
        return product;
    }

    public List<ProductDTO> getAllProducts(){
        final List<Product> productList = repo.findAll();
        final List<ProductDTO> productDtoList = 
                productList.stream().map(ProductDTO::getProductDTO)
                    .collect(Collectors.toList());
        return productDtoList;
    }
    
    @Transactional
    public ProductDTO updateProduct(ProductDTO productDto) {
        final Product productToUpdate = getProductById(productDto.getId());
        productToUpdate.setUpdatedAt(Instant.now());
        productToUpdate.setName(productDto.getName());
        productToUpdate.setDescription(productDto.getDescription());
        productToUpdate.setPrice(productDto.getPrice());
        
        final Product updateProduct = saveProduct(productToUpdate);
        return ProductDTO.getProductDTO(updateProduct);
    }
    
    @Transactional
    public Long deleteProduct(Long productId) {
        final Product productToBeDeleted = getProductById(productId);
        repo.delete(productToBeDeleted);
        return productId;
    }

    private Product getProductById(Long productId) {
        final Product product = repo.findById(productId).orElseThrow(()-> new ProductNotFoundException(productId));
        return product;
    }
    
    private Product saveProduct(Product product) {
        return repo.save(product);
    }
}
