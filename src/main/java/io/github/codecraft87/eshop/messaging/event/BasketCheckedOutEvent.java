package io.github.codecraft87.eshop.messaging.event;

import java.util.List;

public record BasketCheckedOutEvent(
    Long basketId,
    Long userId,
    List<BasketItemEvent> items) {
}
