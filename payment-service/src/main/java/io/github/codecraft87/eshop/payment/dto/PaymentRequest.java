package io.github.codecraft87.eshop.payment.dto;

import io.github.codecraft87.eshop.payment.enums.PaymentMode;
import io.github.codecraft87.eshop.payment.enums.PaymentStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@SuperBuilder
public class PaymentRequest {

  private Long paymentId;

  @NotNull(message = "{order.id.notnull}")
  private Long orderId;

  private PaymentStatus status;

  private PaymentMode paymentMode;
}
