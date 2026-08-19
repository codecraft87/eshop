package io.github.codecraft87.eshop.messagingcommon.publishing;

public interface EventPublisher {
    void publish(String exchange, String routingKey, String payload);
}
