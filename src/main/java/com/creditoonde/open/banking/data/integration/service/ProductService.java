package com.creditoonde.open.banking.data.integration.service;

import com.creditoonde.open.banking.data.integration.dto.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {
    private static final Duration REQUEST_TIMEOUT = Duration.ofSeconds(5);
    private static final String URI_PRODUCTS = "/products";

    private final WebClient simulationClient;

    @Autowired
    public ProductService(@Qualifier("simulation") WebClient simulationClient) {
        this.simulationClient = simulationClient;
    }

    public List<Product> fetchProducts() {
        return simulationClient
                .get()
                .uri(URI_PRODUCTS)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToFlux(Product.class)
                .collectList()
                .block(REQUEST_TIMEOUT);
    }

    public Mono<Product> sendProductToSimulation(Product product) {
        return simulationClient
                .post()
                .uri(URI_PRODUCTS)
                .bodyValue(product)
                .exchangeToMono(clientResponse -> clientResponse.bodyToMono(Product.class));
    }

    public List<Product> sendAllProductsToSimulation(List<Product> products) {
        List<Mono<Product>> monoList = products.stream()
                .map(Mono::just)
                .map(productMono -> productMono
                        .subscribeOn(Schedulers.boundedElastic())
                        .flatMap(this::sendProductToSimulation))
                .collect(Collectors.toList());
        Flux<Product> merged = Flux.merge(monoList);
        return merged.collectList().block(REQUEST_TIMEOUT);
    }
}
