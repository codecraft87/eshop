package io.github.codecraft87.eshop.order.messaging.event;

import io.github.codecraft87.eshop.order.enums.PaymentMode;

public record PaymentRequested(Long orderId, PaymentMode paymentMode, String eventId) {}
