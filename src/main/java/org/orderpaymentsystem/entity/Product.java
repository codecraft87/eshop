package org.orderpaymentsystem.entity;

import java.time.Instant;

import org.orderpaymentsystem.dto.ProductDTO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "product")
@Data
@NoArgsConstructor
public class Product {

    private void set(ProductDTO dto) {
       this.id = dto.getId();
       this.name = dto.getName();
       this.description = dto.getDescription();
       this.price = dto.getPrice();
    }
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
      
    @Column(name = "PRODUCT_NAME")
    private String name;
    
    @Column(name = "PRODUCT_DESCRIPTION")
    private String description;
    
    @Column(name = "PRODUCT_PRICE")
    private Double price;
    
    @Column(name = "CREATED_AT")
    private Instant createdAt;

    @Column(name = "UPDATED_AT")
    private Instant updatedAt;
    
    public static Product getProductEntity(ProductDTO dto) {
        Product product = new Product();
        product.set(dto);
        return product;
    }
}
