package com.creditoonde.open.banking.data.integration.controller;

import com.creditoonde.open.banking.data.integration.dto.Brand;
import com.creditoonde.open.banking.data.integration.dto.Product;
import com.creditoonde.open.banking.data.integration.service.IntegrationService;
import com.creditoonde.open.banking.data.integration.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/product-integration")
public class ProductIntegrationController {

    @Autowired
    private ProductService productService;

    @Autowired
    private IntegrationService integrationService;

    @PostMapping
    public ResponseEntity<List<Product>> fetchAndSendProductsList() {
        List<Brand> brands = integrationService.fetchPersonalLoansData();
        List<Product> products = new ArrayList<>();
        brands.forEach(brand -> products.addAll(integrationService.companiesToProducts(brand.getCompanies())));
        productService.sendAllProductsToSimulation(products);
        return ResponseEntity.ok().body(products);
    }
}
