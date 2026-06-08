package io.github.codecraft87.eshop.payment.dto;

import java.time.Instant;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Setter
@Getter
@SuperBuilder
public class PaymentResponse extends PaymentRequest {

    private Long paymentId;
    
    private Instant createdAt;

    private Instant updatedAt;

}
