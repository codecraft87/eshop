package io.github.codecraft87.eshop.securitycommon;

import java.io.IOException;

import jakarta.servlet.http.HttpServletResponse;

public class ErrorResponseWriter {

  public void writeInvalidJWTToken(HttpServletResponse response) throws IOException {
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    response.setContentType("application/json");

    response.getWriter().write("""
        {
          "errorCode":"%s",
          "status":401,
          "message":"%s"
        }
        """.formatted(SecurityErrorCode.INVALID_JWT_TOKEN.getErrorCode(),
        SecurityErrorCode.INVALID_JWT_TOKEN.getErrorMessage()));
  }
}
