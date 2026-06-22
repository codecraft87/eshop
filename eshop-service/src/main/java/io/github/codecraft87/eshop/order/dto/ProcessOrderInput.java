package io.github.codecraft87.eshop.order.dto;

import io.github.codecraft87.eshop.order.enums.PaymentMode;

public record ProcessOrderInput(Long orderId, PaymentMode paymentMode) {}
