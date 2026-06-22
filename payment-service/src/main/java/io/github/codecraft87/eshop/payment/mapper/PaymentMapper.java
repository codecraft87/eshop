package io.github.codecraft87.eshop.payment.mapper;

import io.github.codecraft87.eshop.payment.dto.PaymentRequest;
import io.github.codecraft87.eshop.payment.dto.PaymentResponse;
import io.github.codecraft87.eshop.payment.entity.Payment;

public class PaymentMapper {

  public static PaymentRequest getPaymentRequest(Payment payment) {
    return PaymentRequest.builder()
        .paymentId(payment.getId())
        .orderId(payment.getOrderId())
        .status(payment.getStatus())
        .build();
  }

  public static Payment getPaymentEntity(PaymentRequest paymentRequest) {
    return Payment.builder().orderId(paymentRequest.getOrderId()).build();
  }

  public static PaymentResponse getPaymentResponse(Payment payment) {
    return PaymentResponse.builder()
        .paymentId(payment.getId())
        .orderId(payment.getOrderId())
        .status(payment.getStatus())
        .createdAt(payment.getCreatedAt())
        .updatedAt(payment.getUpdatedAt())
        .build();
  }
}
