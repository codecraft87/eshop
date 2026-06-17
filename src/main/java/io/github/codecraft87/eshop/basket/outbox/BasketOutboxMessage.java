package io.github.codecraft87.eshop.basket.outbox;

import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "basket_outbox_event", schema = "basket")
public class BasketOutboxMessage {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "event_id", nullable = false, unique = true)
    private UUID eventId;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "event_type")
    private OutboxEventType eventType;
    
    @Column(columnDefinition = "TEXT")
    private String payload;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "event_status")
    private OutboxEventStatus status;
    
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "published_at")
    private Instant publishedAt;
    
    @Column(name = "retry_count")
    private Integer retryCount;
    
    @Column(name = "last_error")
    private String lastError;
    
    public void markPublished() {
        this.setPublishedAt(Instant.now());
        this.setStatus(OutboxEventStatus.PUBLISHED);
    }
    
    public void markFailed(String errorMessage) {
        this.setRetryCount(this.getRetryCount()+1);
        this.setStatus(OutboxEventStatus.FAILED);
        this.setLastError(errorMessage);
    }
}
