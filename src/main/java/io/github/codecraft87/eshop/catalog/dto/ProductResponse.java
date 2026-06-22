package io.github.codecraft87.eshop.catalog.dto;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class ProductResponse extends ProductRequest {

  private Long id;

  private Instant createdAt;

  private Instant updatedAt;
}
