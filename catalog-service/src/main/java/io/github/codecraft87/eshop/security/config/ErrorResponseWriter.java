package io.github.codecraft87.eshop.security.config;

import java.io.IOException;

import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletResponse;

@Component
public class ErrorResponseWriter {

    public void writeDatabaseUnavailable(HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
        response.setContentType("application/json");

        response.getWriter().write("""
                {
                  "errorCode":"DB_NOT_AVAILABLE",
                  "status":503,
                  "message":"Database is currently unavailable. Please try again later."
                }
                """);
    }
}
