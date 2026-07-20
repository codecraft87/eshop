package io.github.codecraft87.eshop.payment.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;
import lombok.NoArgsConstructor;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "id", "status", "message", "data" })
@Data
@NoArgsConstructor
public class OperationResponse<T> {

  private Long id;

  private String message;

  private T data;

  public OperationResponse(Long id, String message) {
    this.id = id;
    this.message = message;
  }
}