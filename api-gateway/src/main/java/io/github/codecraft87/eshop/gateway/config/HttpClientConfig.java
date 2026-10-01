package io.github.codecraft87.eshop.gateway.config;

import org.springframework.boot.http.client.ClientHttpRequestFactoryBuilder;
import org.springframework.boot.http.client.HttpComponentsClientHttpRequestFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HttpClientConfig {

    @Bean
    HttpComponentsClientHttpRequestFactoryBuilder httpComponentsClientHttpRequestFactoryBuilder() {
        return ClientHttpRequestFactoryBuilder.httpComponents()
                .withHttpClientCustomizer(
                        builder -> builder.disableAutomaticRetries());
    }
}
