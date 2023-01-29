package com.creditoonde.open.banking.data.integration.service;

import com.creditoonde.open.banking.data.integration.dto.Product;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @Test
    void shouldRetrieveAllProducts() throws JsonProcessingException {
        List<Product> products = productService.retrieveProducts();
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(products);
        System.out.println(json);

        Assertions.assertEquals("Easy loan", products.get(0).getName());
    }

    @Test
    void shouldSendProductToSimulation() {
        Product product = new Product("Test", "Test", 1.59, 2.1, "Test");
        productService.sendProductToSimulation(product);
    }

    @Test
    void shouldSendProductsListToSimulation() {
        List<Product> products = Arrays.asList(Product.builder()
                        .name("LOAN_TEST").rateIndexer("INDEXER_TEST")
                        .minInterestRate(1.99)
                        .maxInterestRate(3.77)
                        .financialInstitution("TEST BANK SA")
                        .build(),
                Product.builder()
                        .name("LOAN_TEST_2").rateIndexer("INDEXER_TEST_3")
                        .minInterestRate(1.09)
                        .maxInterestRate(7.78)
                        .financialInstitution("TEST BANK SA")
                        .build());
        productService.sendAllProductsToSimulation(products);
    }
}