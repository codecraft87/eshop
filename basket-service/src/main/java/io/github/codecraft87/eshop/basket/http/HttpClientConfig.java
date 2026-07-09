package io.github.codecraft87.eshop.basket.http;

import java.time.Duration;

import org.springframework.boot.http.client.ClientHttpRequestFactoryBuilder;
import org.springframework.boot.http.client.HttpClientSettings;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import io.github.codecraft87.eshop.basket.catalog.CatalogClient;

@Configuration
public class HttpClientConfig {

  @Bean
  RestClient restClient(RestClient.Builder builder, JwtTokenInterceptor interceptor) {
      HttpClientSettings settings = HttpClientSettings.defaults()
              .withConnectTimeout(Duration.ofSeconds(3))
              .withReadTimeout(Duration.ofSeconds(10));
      ClientHttpRequestFactory requestFactory = ClientHttpRequestFactoryBuilder
              .detect()
              .build(settings);
    return builder
           .requestFactory(requestFactory)
           .requestInterceptor(interceptor)
           .build();
  }

  @Bean
  CatalogClient catalogClient(RestClient restClient) {
      RestClientAdapter adapter = RestClientAdapter.create(restClient);
      HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).
              build();
    

    return factory.createClient(CatalogClient.class);
  }
}
