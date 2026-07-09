package io.github.codecraft87.eshop.basket.exceptions;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {

  private String errorCode;
  private int status;
  private String message;
  private Instant timestamp;
}
