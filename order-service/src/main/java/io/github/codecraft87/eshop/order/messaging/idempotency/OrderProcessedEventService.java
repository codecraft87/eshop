package io.github.codecraft87.eshop.order.messaging.idempotency;

import java.time.Instant;
import java.util.UUID;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderProcessedEventService {

  private final OrderProcessedEventRepository processedEventRepository;

  public boolean checkIfEventIsProcessed(UUID eventId) {
    return processedEventRepository.existsById(eventId);
  }

  public void addProcessedEventEntry(UUID eventId) {
    OrderProcessedEvent procssedEvent = new OrderProcessedEvent();
    procssedEvent.setEventId(eventId);
    procssedEvent.setProcessAt(Instant.now());
    processedEventRepository.save(procssedEvent);
  }
}
