package io.github.codecraft87.eshop.payment.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.codecraft87.eshop.payment.dto.OperationResponse;
import io.github.codecraft87.eshop.payment.dto.PaymentRequest;
import io.github.codecraft87.eshop.payment.dto.PaymentResponse;
import io.github.codecraft87.eshop.payment.service.PaymentService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/payments")
public class PaymentController {

  private final PaymentService paymentService;

  public PaymentController(PaymentService service) {
    this.paymentService = service;
  }

  @GetMapping("/about")
  public ResponseEntity<String> about() {
    return ResponseEntity.ok().body("<h1>Payment Service is running.</h1>");
  }

  @PostMapping
  public ResponseEntity<OperationResponse> processPayment(
      @Valid @RequestBody PaymentRequest paymentRequest) {
    Long paymentId = paymentService.processPayment(paymentRequest);
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(new OperationResponse(paymentId, "Payment processed"));
  }

  @PutMapping("/{paymentId}/retry")
  public ResponseEntity<OperationResponse> retryPayment(@PathVariable("paymentId") Long paymentId) {
    Long payId = paymentService.retryPayment(paymentId);
    return ResponseEntity.status(HttpStatus.OK)
        .body(new OperationResponse(payId, "Payment processed"));
  }

  @GetMapping("/{paymentId}")
  public ResponseEntity<PaymentResponse> getPaymentDetails(
      @PathVariable("paymentId") Long paymentId) {
    PaymentResponse paymentResponse = paymentService.getPaymentDetails(paymentId);
    return ResponseEntity.ok().body(paymentResponse);
  }
}
