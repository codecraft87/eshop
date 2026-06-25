package io.github.codecraft87.eshop.catalog.entity;

import java.time.Instant;

import io.github.codecraft87.eshop.catalog.common.enums.EventStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "product_out_box_event", schema = "catalog")
public class ProductOutBox {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "event_id")
  private String eventId;

  @Column(name = "event_type")
  private String eventType;

  private String payload;

  @Column(name = "event_status")
  private EventStatus status;

  @Column(name = "created_at")
  private Instant createdAt;

  @Column(name = "published_at")
  private Instant publishedAt;

  @Column(name = "retry_count")
  private Integer retryCount;

  @Column(name = "last_count")
  private String lastError;
}
