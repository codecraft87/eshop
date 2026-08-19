package io.github.codecraft87.eshop.payment.messaging.idempotency;

import java.time.Instant;
import java.util.UUID;

import org.springframework.stereotype.Service;

import io.github.codecraft87.eshop.messagingcommon.idempotency.EventIdempotency;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentProcessedEventService implements EventIdempotency {

  private final PaymentProcessedEventRepository processedEventRepository;

  @Override
  public boolean isEventProcessed(UUID eventId) {
    return processedEventRepository.existsById(eventId);
  }

  @Override
  public void saveProcessedEvent(UUID eventId) {
    PaymentProcessedEvent procssedEvent = new PaymentProcessedEvent();
    procssedEvent.setEventId(eventId);
    procssedEvent.setProcessAt(Instant.now());
    processedEventRepository.save(procssedEvent);
  }
}
