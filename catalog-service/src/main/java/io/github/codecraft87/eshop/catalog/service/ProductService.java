package io.github.codecraft87.eshop.catalog.service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import io.github.codecraft87.eshop.catalog.dto.ProductRequest;
import io.github.codecraft87.eshop.catalog.dto.ProductResponse;
import io.github.codecraft87.eshop.catalog.entity.Product;
import io.github.codecraft87.eshop.catalog.exceptions.ProductNotFoundException;
import io.github.codecraft87.eshop.catalog.mapper.ProductMapper;
import io.github.codecraft87.eshop.catalog.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProductService implements CatalogModuleService {

  private final ProductRepository catalogRepository;

  @Transactional
  public Long addProduct(ProductRequest productRequest) {
    log.info("Adding product {} ", productRequest.getName());
    final Product product = ProductMapper.getProductEntity(productRequest);
    product.setCreatedAt(Instant.now());
    Long productId = saveProduct(product).getId();
    log.info("Product added successfully");
    return productId;
  }

  public ProductResponse getProductDetails(Long productId) {

    final Product product = getProduct(productId);

    return ProductMapper.getProductResponse(product);
  }

  public List<ProductResponse> getAllProducts() {
    log.info("Getting All product ");
    final List<Product> productList = catalogRepository.findAll();
    final List<ProductResponse> productDtoList = productList.stream().map(ProductMapper::getProductResponse)
        .collect(Collectors.toList());
    return productDtoList;
  }

  @Transactional
  public ProductResponse updateProduct(Long productId, ProductRequest productRequest) {
    log.info("Modifying product {} ", productId);
    final Product productToUpdate = getProduct(productId);
    productToUpdate.setUpdatedAt(Instant.now());
    productToUpdate.setName(productRequest.getName());
    productToUpdate.setDescription(productRequest.getDescription());
    productToUpdate.setPrice(productRequest.getPrice());

    final Product updatedProduct = saveProduct(productToUpdate);
    log.info("Product updated successfully");
    return ProductMapper.getProductResponse(updatedProduct);
  }

  @Transactional
  public Long deleteProduct(Long productId) {
    log.info("Deleting product {} ", productId);
    final Product productToBeDeleted = getProduct(productId);
    catalogRepository.delete(productToBeDeleted);
    log.info("Product deleted successfully");
    return productId;
  }

  public Product getProduct(Long productId) {
    log.info("Getting product details {} ", productId);
    final Product product = catalogRepository
        .findById(productId)
        .orElseThrow(() -> new ProductNotFoundException(productId));
    return product;
  }

  private Product saveProduct(Product product) {
    product.setUpdatedAt(Instant.now());
    return catalogRepository.save(product);
  }
}
