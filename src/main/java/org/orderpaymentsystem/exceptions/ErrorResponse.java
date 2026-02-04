package org.orderpaymentsystem.exceptions;

import java.time.Instant;

import org.orderpaymentsystem.common.enums.ErrorEnums;

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