package com.creditoonde.open.banking.data.integration.service;

import com.creditoonde.open.banking.data.integration.dto.Brand;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.time.Duration;

@Service
public class IntegrationService {
    private static final Duration REQUEST_TIMEOUT = Duration.ofSeconds(5);
    private static final String URI_PERSONAL_LOANS = "/personal-loans/?page=3&page-size=1";

    private final WebClient client;

    @Autowired
    public IntegrationService(WebClient client) {
        this.client = client;
    }

    public Brand retrievePersonalLoansData() {
        ObjectMapper mapper = new ObjectMapper();
        return client
                .get()
                .uri(URI_PERSONAL_LOANS)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .map(brand -> brand.path("data").path("brand"))
                .map(brand -> {
                    try {
                        return mapper.readValue(brand.traverse(), Brand.class);
                    } catch (IOException e) {
                        e.printStackTrace();
                        return new Brand();
                    }
                })
                .block(REQUEST_TIMEOUT);
    }
}
