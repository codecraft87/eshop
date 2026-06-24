package io.github.codecraft87.eshop.order.messaging.event;

import java.util.List;

public record BasketCheckedOutEvent(
    String eventId, Long basketId, Long userId, List<BasketItemEvent> items) {}
