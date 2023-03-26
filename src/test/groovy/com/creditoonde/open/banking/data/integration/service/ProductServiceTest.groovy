package com.creditoonde.open.banking.data.integration.service

import com.creditoonde.open.banking.data.integration.dto.Product
import com.fasterxml.jackson.databind.ObjectMapper
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import spock.lang.Shared
import spock.lang.Specification

import java.time.Duration

class ProductServiceTest extends Specification {

    @Shared
    WebClient simulationClient
    @Shared
    ProductService productService
    @Shared
    MockWebServer mockWebServer

    def setupSpec() {
        mockWebServer = new MockWebServer()
        mockWebServer.start()
        simulationClient = WebClient.create(String.format('http://localhost:%s', mockWebServer.getPort()))
        productService = new ProductService(simulationClient)
    }

    def 'Should return the list of products available for simulation'() {
        given:
        def expectedProducts =
                [new Product('product1', 'indexer1', 1.0, 2.0, 'institution1'),
                 new Product('product2', 'indexer2', 3.0, 4.0, 'institution2')]
        mockWebServer
                .enqueue(new MockResponse().setBody(new ObjectMapper().writeValueAsString(expectedProducts))
                        .addHeader('Content-Type', MediaType.APPLICATION_JSON))

        when:
        def result = Mono.just(productService.fetchProducts())

        then:
        StepVerifier.create(result)
                .expectNextSequence(Arrays.asList(expectedProducts))
                .expectComplete()
                .verify(Duration.ofSeconds(5))
    }

    def 'Should send a product to simulation API and return it in response body'() {
        given:
        def expectedProduct = new Product('product1', 'indexer1', 1.0, 2.0, 'institution1')
        mockWebServer
                .enqueue(new MockResponse().setBody(new ObjectMapper().writeValueAsString(expectedProduct))
                        .addHeader('Content-Type', MediaType.APPLICATION_JSON))

        when:
        def result = productService.sendProductToSimulation(expectedProduct)

        then:
        StepVerifier.create(result)
                .expectNextMatches(product -> {
                    product.getName() == expectedProduct.getName() &&
                            product.getRateIndexer() == expectedProduct.getRateIndexer() &&
                            product.getMinInterestRate() == expectedProduct.getMinInterestRate() &&
                            product.getMaxInterestRate() == expectedProduct.getMaxInterestRate() &&
                            product.getFinancialInstitution() == expectedProduct.getFinancialInstitution()
                })
                .verifyComplete()
    }

    def 'Should send a list of products to simulation API and return it in response body'() {
        given:
        def expectedProducts =
                [new Product('product1', 'indexer1', 1.0, 2.0, 'institution1'),
                 new Product('product2', 'indexer2', 3.0, 4.0, 'institution2')]
        expectedProducts.forEach(expectedProduct -> {
            mockWebServer
                    .enqueue(new MockResponse().setBody(new ObjectMapper().writeValueAsString(expectedProduct))
                            .addHeader('Content-Type', MediaType.APPLICATION_JSON))
        })

        when:
        def result = Mono.just(productService.sendAllProductsToSimulation(expectedProducts))

        then:
        StepVerifier.create(result)
                .expectNextSequence(Arrays.asList(expectedProducts))
                .expectComplete()
                .verify(Duration.ofSeconds(5))
    }

    def cleanupSpec() {
        mockWebServer.shutdown()
    }
}
