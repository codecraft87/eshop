package io.github.codecraft87.eshop.securitycommon;

import java.io.IOException;

import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletResponse;

@Component
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
                """.formatted(ErrorEnums.INVALID_JWT_TOKEN.getErrorCode(),
                ErrorEnums.INVALID_JWT_TOKEN.getErrorMessage()));
    }
}
