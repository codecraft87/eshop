package io.github.codecraft87.eshop.messagingcommon.outbox;

public interface OutboxPublisher {
    void publishPendingEvents();
}
