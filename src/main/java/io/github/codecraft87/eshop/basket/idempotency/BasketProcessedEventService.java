package io.github.codecraft87.eshop.basket.idempotency;

import java.time.Instant;
import java.util.UUID;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BasketProcessedEventService {

  private final BasketProcessedEventRepository processedEventRepository;

  public boolean checkIfEventIsProcessed(UUID eventId) {
    return processedEventRepository.existsById(eventId);
  }

  public void addProcessedEventEntry(UUID eventId) {
    BasketProcessedEvent basketCheckedOutProcessedEvent = new BasketProcessedEvent();
    basketCheckedOutProcessedEvent.setEventId(eventId);
    basketCheckedOutProcessedEvent.setProcessAt(Instant.now());
    processedEventRepository.save(basketCheckedOutProcessedEvent);
  }
}
