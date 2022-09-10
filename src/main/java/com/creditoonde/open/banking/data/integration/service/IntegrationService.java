package com.creditoonde.open.banking.data.integration.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;

@Service
public class IntegrationService {
    private static final Duration REQUEST_TIMEOUT = Duration.ofSeconds(5);

    private final WebClient client;

    @Autowired
    public IntegrationService(WebClient client) {
        this.client = client;
    }

    public String retrievePersonalLoansData() {
        return client
                .get()
                .uri("/personal-loans")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(String.class)
                .block(REQUEST_TIMEOUT);
    }
}
