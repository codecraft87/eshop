package io.github.codecraft87.eshop.payment.service;

public interface PaymentModuleService {

    void handleOrderCancellation(Long orderId);
}
