package io.github.codecraft87.eshop.payment.exceptions;

import java.time.Instant;

import io.github.codecraft87.eshop.payment.enums.ErrorEnums;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {

  private ErrorEnums errorCode;
  private int status;
  private String message;
  private Instant timestamp;
}
