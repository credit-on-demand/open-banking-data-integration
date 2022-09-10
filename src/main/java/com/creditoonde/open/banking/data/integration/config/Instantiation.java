package com.creditoonde.open.banking.data.integration.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class Instantiation {

    @Bean
    public WebClient itauApiClient() {
        return WebClient.create("https://api.itau/open-banking/products-services/v1");
    }
}
