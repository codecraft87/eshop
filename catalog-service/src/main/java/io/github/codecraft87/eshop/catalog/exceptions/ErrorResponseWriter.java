package io.github.codecraft87.eshop.catalog.exceptions;

import java.io.IOException;

import org.springframework.stereotype.Component;

import io.github.codecraft87.eshop.catalog.common.enums.ErrorEnums;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class ErrorResponseWriter {

  public void writeDatabaseUnavailable(HttpServletResponse response) throws IOException {
    response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
    response.setContentType("application/json");

    response.getWriter().write("""
        {
          "errorCode":"%s",
          "status":503,
          "message":"%s"
        }
        """.formatted(ErrorEnums.DB_NOT_AVAILABLE.getErrorCode(),
        ErrorEnums.DB_NOT_AVAILABLE.getErrorMessage()));
  }

  public void writeInvalidJWTToken(HttpServletResponse response) throws IOException {
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    response.setContentType("application/json");

    response.getWriter().write("""
        {
          "errorCode":"%s",
          "status":401,
          "message":"%s"
        }
        """.formatted(ErrorEnums.INVALID_JWT_TOKEN.getErrorCode(),
        ErrorEnums.INVALID_JWT_TOKEN.getErrorMessage()));
  }
}
