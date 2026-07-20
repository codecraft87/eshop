package io.github.codecraft87.eshop.payment.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.codecraft87.eshop.payment.common.constants.ResponseMessageConstant;
import io.github.codecraft87.eshop.payment.dto.OperationResponse;
import io.github.codecraft87.eshop.payment.dto.PaymentResponse;
import io.github.codecraft87.eshop.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

  private final PaymentService paymentService;

  @GetMapping("/about")
  public ResponseEntity<String> about() {
    return ResponseEntity.ok().body("<h1>Payment Service is running.</h1>");
  }

  @PutMapping("/{paymentId}/retry")
  public ResponseEntity<OperationResponse<Object>> retryPayment(@PathVariable("paymentId") Long paymentId) {
    log.info("Received request for retry processing payment");
    Long payId = paymentService.retryPayment(paymentId);
    return ResponseEntity.status(HttpStatus.OK)
        .body(new OperationResponse<>(payId, ResponseMessageConstant.PAYMENT_PROCESSED));
  }

  @GetMapping("/{paymentId}")
  public ResponseEntity<OperationResponse<PaymentResponse>> getPaymentDetails(
      @PathVariable("paymentId") Long paymentId) {
    log.info("Received request for getting payment");
    PaymentResponse paymentResponse = paymentService.getPaymentDetails(paymentId);
    OperationResponse<PaymentResponse> response = new OperationResponse<>(paymentId,
        ResponseMessageConstant.PAYMENT_DETAILS);
    response.setData(paymentResponse);

    return ResponseEntity.ok().body(response);
  }
}
