package io.github.codecraft87.eshop.basket.messaging.idempotency;

import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "processed_event", schema = "basket")
public class BasketProcessedEvent {
  @Id private UUID eventId;

  private Instant processAt;
}
