package io.github.codecraft87.eshop.basket.http;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import io.github.codecraft87.eshop.basket.catalog.CatalogClient;

@Configuration
public class HttpClientConfig {

  @Bean
  RestClient restClient(RestClient.Builder builder, JwtTokenInterceptor interceptor) {
    return builder.requestInterceptor(interceptor).build();
  }

  @Bean
  CatalogClient catalogClient(RestClient restClient) {
    RestClientAdapter adapter = RestClientAdapter.create(restClient);
    HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();
    return factory.createClient(CatalogClient.class);
  }
}
