package com.creditoonde.open.banking.data.integration.service;

import com.creditoonde.open.banking.data.integration.dto.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.util.List;

@Service
public class ProductService {
    private static final Duration REQUEST_TIMEOUT = Duration.ofSeconds(5);
    private static final String URI_PRODUCTS = "/products";

    private final WebClient simulationClient;

    @Autowired
    public ProductService(@Qualifier("product") WebClient simulationClient) {
        this.simulationClient = simulationClient;
    }

    public List<Product> retrieveProducts() {
        return simulationClient
                .get()
                .uri(URI_PRODUCTS)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToFlux(Product.class)
                .collectList()
                .block(REQUEST_TIMEOUT);
    }

    public Product sendProductToSimulation(Product product) {
        return simulationClient
                .post()
                .uri(URI_PRODUCTS)
                .bodyValue(product)
                .exchangeToMono(clientResponse -> clientResponse.bodyToMono(Product.class))
                .block(REQUEST_TIMEOUT);
    }

    public void sendAllProductsToSimulation(List<Product> products) {
        products.forEach(this::sendProductToSimulation);
    }
}
