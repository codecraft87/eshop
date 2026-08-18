package io.github.codecraft87.eshop.securitycommon;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
public class SecurityCommonAutoConfiguration {

    @Bean
    public JwtTokenValidator jwtTokenValidator() {
        return new JwtTokenValidator();
    }

    @Bean
    public ErrorResponseWriter errorResponseWriter() {
        return new ErrorResponseWriter();
    }

    @Bean
    public JwtFilter jwtFilter(JwtTokenValidator jwtTokenValidator, ErrorResponseWriter errorResponseWriter) {
        return new JwtFilter(errorResponseWriter, jwtTokenValidator);
    }
}
