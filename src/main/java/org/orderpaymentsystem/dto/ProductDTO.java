package org.orderpaymentsystem.dto;


import java.time.Instant;

import org.orderpaymentsystem.entity.Product;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProductDTO {

    private void set(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.description = product.getDescription();
        this.price = product.getPrice();
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
