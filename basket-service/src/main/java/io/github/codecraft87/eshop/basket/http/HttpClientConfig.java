package io.github.codecraft87.eshop.basket.http;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.support.RestClientHttpServiceGroupConfigurer;
import org.springframework.web.service.registry.ImportHttpServices;

import io.github.codecraft87.eshop.basket.catalog.CatalogClient;

@Configuration
@ImportHttpServices(group = "catalog-service", types = { CatalogClient.class })
public class HttpClientConfig {

        @Bean
        RestClientHttpServiceGroupConfigurer httpServiceGroupConfigurer(
                        JwtTokenInterceptor interceptor) {

                return groups -> groups
                                .filterByName("catalog-service")
                                .forEachClient((group, clientBuilder) -> clientBuilder.requestInterceptor(interceptor));
        }
}
