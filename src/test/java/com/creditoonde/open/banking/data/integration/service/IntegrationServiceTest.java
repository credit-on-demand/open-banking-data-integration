package com.creditoonde.open.banking.data.integration.service;

import com.creditoonde.open.banking.data.integration.dto.Brand;
import com.creditoonde.open.banking.data.integration.dto.Product;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@SpringBootTest
class IntegrationServiceTest {

    @Autowired
    private IntegrationService integrationService;

    @Test
    void shouldLoadIntegrationData() throws JsonProcessingException {
        List<Brand> brands = integrationService.fetchPersonalLoansData();
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(brands);
        brands.sort(Comparator.comparing(Brand::getName));

        System.out.println(json);
        Assertions.assertEquals("Grupo Pan", brands.get(0).getName());
        Assertions.assertEquals("Itaú", brands.get(1).getName());
    }

    @Test
    void shouldCreateListOfProductsFromIntegrationData() throws JsonProcessingException {
        List<Brand> brands = integrationService.fetchPersonalLoansData();
        List<Product> products = new ArrayList<>();
        brands.forEach(brand -> {
            products.addAll(integrationService.companiesToProducts(brand.getCompanies()));
        });
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(products);

        System.out.println(json);
        Assertions.assertNotEquals(0, products.size());
    }

}