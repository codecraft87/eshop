package io.github.codecraft87.eshop.order.messaging.idempotency;

import java.time.Instant;
import java.util.UUID;

import org.springframework.stereotype.Service;

import io.github.codecraft87.eshop.messagingcommon.idempotency.EventIdempotency;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderProcessedEventService implements EventIdempotency {

  private final OrderProcessedEventRepository processedEventRepository;

  @Override
  public boolean isEventProcessed(UUID eventId) {
    return processedEventRepository.existsById(eventId);
  }

  @Override
  public void saveProcessedEvent(UUID eventId) {
    OrderProcessedEvent procssedEvent = new OrderProcessedEvent();
    procssedEvent.setEventId(eventId);
    procssedEvent.setProcessAt(Instant.now());
    processedEventRepository.save(procssedEvent);
  }
}
