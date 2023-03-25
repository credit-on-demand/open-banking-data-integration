package com.creditoonde.open.banking.data.integration.service;

import com.creditoonde.open.banking.data.integration.dto.Brand;
import com.creditoonde.open.banking.data.integration.dto.Company;
import com.creditoonde.open.banking.data.integration.dto.Product;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static com.creditoonde.open.banking.data.integration.helper.NumberFormatHelper.formatToDouble;

@Service
public class IntegrationService {
    private static final Duration REQUEST_TIMEOUT = Duration.ofSeconds(5);
    private static final String URI_PERSONAL_LOANS = "/personal-loans";

    private final List<WebClient> integrationClients;

    @Autowired
    public IntegrationService(@Qualifier("integration") List<WebClient> integrationClients) {
        this.integrationClients = integrationClients;
    }

    public List<Brand> fetchPersonalLoansData() {
        ObjectMapper mapper = new ObjectMapper();
        List<Brand> brands;
        Flux<Mono<Brand>> brandMonos =
                Flux.fromIterable(integrationClients)
                        .flatMap(client -> client.get()
                                .uri(URI_PERSONAL_LOANS)
                                .accept(MediaType.APPLICATION_JSON)
                                .retrieve()
                                .bodyToMono(JsonNode.class)
                                .map(brand -> brand.path("data").path("brand"))
                                .map(brand -> {
                                    try {
                                        Brand b = mapper.readValue(brand.traverse(), Brand.class);
                                        return Mono.just(b);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                        return Mono.empty();
                                    }
                                }));
        Flux<Brand> brandFlux = Flux.merge(brandMonos);
        brands = brandFlux.collectList().block(REQUEST_TIMEOUT);
        return brands;
    }

    public List<Product> companiesToProducts(List<Company> companies) {
        List<Product> products = new ArrayList<>();
        companies
                .forEach(company -> company.getPersonalLoans()
                        .forEach(personalLoan -> personalLoan.getInterestRates()
                                .forEach(interestRate -> {
                                    Product product = Product.builder()
                                            .name(personalLoan.getType())
                                            .rateIndexer(interestRate.getReferentialRateIndexer())
                                            .financialInstitution(company.getName())
                                            .minInterestRate(formatToDouble(interestRate.getMinimumRate()))
                                            .maxInterestRate(formatToDouble(interestRate.getMaximumRate()))
                                            .build();
                                    products.add(product);
                                })));
        return products;
    }
}
