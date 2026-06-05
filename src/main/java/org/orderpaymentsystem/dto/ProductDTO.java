package org.orderpaymentsystem.dto;


import java.time.Instant;

import org.orderpaymentsystem.entity.Product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDTO {

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
    
    public static  ProductDTO getProductDTO(Product product) {
        final ProductDTO dto = new ProductDTO();
        dto.set(product);
        return dto;
    }
}
