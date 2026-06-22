package io.github.codecraft87.eshop.payment.messaging.event;

import io.github.codecraft87.eshop.payment.enums.PaymentMode;

public record PaymentRequested(Long orderId, PaymentMode paymentMode, String eventId) {}
