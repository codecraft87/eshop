package io.github.codecraft87.eshop.payment.idempotency;

import java.time.Instant;
import java.util.UUID;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentProcessedEventService {

    private final PaymentProcessedEventRepository processedEventRepository;
    
    public boolean checkIfEventIsProcessed(UUID eventId) {
        return processedEventRepository.existsById(eventId);
    }
    
    public void addProcessedEventEntry(UUID eventId) {
        PaymentProcessedEvent procssedEvent = 
                                        new PaymentProcessedEvent();
        procssedEvent.setEventId(eventId);
        procssedEvent.setProcessAt(Instant.now());
        processedEventRepository.save(procssedEvent);
    }
}