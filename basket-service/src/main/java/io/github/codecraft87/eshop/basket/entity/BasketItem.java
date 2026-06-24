package io.github.codecraft87.eshop.basket.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "basket_item", schema = "basket")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BasketItem {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "basket_id")
  private Basket basket;

  @JoinColumn(name = "product_id")
  private Long productId;

  @JoinColumn(name = "product_id")
  private String productName;

  @Column(name = "unit_price")
  private Double unitPrice;

  @Column(name = "quantity")
  private Integer quantity;
}
