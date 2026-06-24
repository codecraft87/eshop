package io.github.codecraft87.eshop.basket.entity;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import io.github.codecraft87.eshop.basket.enums.BasketStatus;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "basket", schema = "basket")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Basket {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "user_id")
  private Long userId;

  @Enumerated(EnumType.STRING)
  @Column(name = "basket_status")
  private BasketStatus status;

  @CreationTimestamp
  @Column(name = "created_at")
  private Instant createdAt;

  @UpdateTimestamp
  @Column(name = "updated_at")
  private Instant updatedAt;

  @OneToMany(mappedBy = "basket", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<BasketItem> items = new ArrayList<>();

  public Optional<BasketItem> findItemByProductId(Long itemId) {

    return items.stream().filter(item -> item.getProductId().equals(itemId)).findFirst();
  }
}
