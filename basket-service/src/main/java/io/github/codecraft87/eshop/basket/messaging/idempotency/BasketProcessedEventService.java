package io.github.codecraft87.eshop.basket.messaging.idempotency;

import java.time.Instant;
import java.util.UUID;

import org.springframework.stereotype.Service;

import io.github.codecraft87.eshop.messagingcommon.idempotency.EventIdempotency;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BasketProcessedEventService implements EventIdempotency {

  private final BasketProcessedEventRepository processedEventRepository;

  @Override
  public boolean isEventProcessed(UUID eventId) {
    return processedEventRepository.existsById(eventId);
  }

  @Override
  public void saveProcessedEvent(UUID eventId) {
    BasketProcessedEvent basketCheckedOutProcessedEvent = new BasketProcessedEvent();
    basketCheckedOutProcessedEvent.setEventId(eventId);
    basketCheckedOutProcessedEvent.setProcessAt(Instant.now());
    processedEventRepository.save(basketCheckedOutProcessedEvent);
  }
}
