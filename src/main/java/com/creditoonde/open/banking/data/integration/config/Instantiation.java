package com.creditoonde.open.banking.data.integration.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableConfigurationProperties(ApiProperties.class)
public class Instantiation {

    @Bean
    @Qualifier("integration")
    public List<WebClient> externalApiClients(ApiProperties apiProperties) {
        List<WebClient> externalApiClients = new ArrayList<>();
        for (String apiUrl : apiProperties.getExternalUrls()) {
            externalApiClients.add(WebClient.create(apiUrl));
        }
        return externalApiClients;
    }

    @Bean
    @Qualifier("simulation")
    public WebClient simulationClient(ApiProperties apiProperties) {
        return WebClient.create(apiProperties.getSimulationUrl());
    }
}
