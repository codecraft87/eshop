package org.orderpaymentsystem.dto;

import java.time.Instant;

import org.orderpaymentsystem.common.enums.PaymentStatus;
import org.orderpaymentsystem.entity.Payment;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PaymentDTO {

    private void set(Payment payment) {
        this.paymentId = payment.getId();
        this.orderId = payment.getOrderId();
        this.createdAt = payment.getCreatedAt();
        this.updatedAt = payment.getUpdatedAt();
        this.status = payment.getStatus();
    }

    private Long paymentId;

    @NotNull(message = "{order.id.notnull}")
    private Long orderId;

    private PaymentStatus status;

    private Instant createdAt;

    private Instant updatedAt;

    private boolean simulateFailure;

    public static PaymentDTO getPaymentDTO(Payment payment) {
        PaymentDTO dto = new PaymentDTO();
        dto.set(payment);
        return dto;
    }
}
