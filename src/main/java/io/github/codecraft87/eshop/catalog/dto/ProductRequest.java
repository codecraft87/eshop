package io.github.codecraft87.eshop.catalog.dto;


import java.time.Instant;

import io.github.codecraft87.eshop.catalog.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequest {

    private void set(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.description = product.getDescription();
        this.price = product.getPrice();
        this.createdAt = product.getCreatedAt();
        this.updatedAt = product.getUpdatedAt();
    }

    private Long id;
    
    private String name;
    
    private String description;
    
    private Double price;
    
    private Instant createdAt;

    private Instant updatedAt;
    
    public static  ProductRequest getProductDTO(Product product) {
        final ProductRequest dto = new ProductRequest();
        dto.set(product);
        return dto;
    }
}
