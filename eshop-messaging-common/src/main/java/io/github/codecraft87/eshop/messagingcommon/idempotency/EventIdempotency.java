package io.github.codecraft87.eshop.messagingcommon.idempotency;

import java.util.UUID;

public interface EventIdempotency {
    boolean isEventProcessed(UUID eventId);

    void saveProcessedEvent(UUID eventId);
}
