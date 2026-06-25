package io.github.codecraft87.eshop.basket.catalog;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.service.registry.ImportHttpServices;

@Configuration
@ImportHttpServices(CatalogClient.class)
public class HttpClientConfig {}
