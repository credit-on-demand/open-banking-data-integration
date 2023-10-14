package com.creditoonde.open.banking.data.integration.service

import com.creditoonde.open.banking.data.integration.dto.Brand
import com.creditoonde.open.banking.data.integration.dto.Company
import com.creditoonde.open.banking.data.integration.dto.InterestRate
import com.creditoonde.open.banking.data.integration.dto.PersonalLoan
import com.fasterxml.jackson.databind.ObjectMapper
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Flux
import reactor.test.StepVerifier
import spock.lang.Shared
import spock.lang.Specification

import java.time.Duration

class IntegrationServiceTest extends Specification {

    @Shared
    IntegrationService integrationService
    @Shared
    WebClient integrationClient
    @Shared
    MockWebServer mockWebServer

    def setupSpec() {
        mockWebServer = new MockWebServer()
        mockWebServer.start()
        integrationClient = WebClient.create(String.format('http://localhost:%s', mockWebServer.getPort()))
        integrationService = new IntegrationService([integrationClient])
    }

    def 'Should return a list of brands correctly'() {
        given:
        def expectedResponse = createMockResponse()
        mockWebServer
                .enqueue(new MockResponse().setBody(new ObjectMapper().writeValueAsString(expectedResponse))
                        .addHeader('Content-Type', MediaType.APPLICATION_JSON))

        when:
        def result = Flux.fromIterable(integrationService.fetchPersonalLoansData())

        then:
        StepVerifier.create(result)
                .expectNext(expectedResponse.data.brand)
                .expectComplete()
                .verify(Duration.ofSeconds(5))
    }

    def 'Should convert a list of companies into a list of products correctly'() {
        given:
        def companies = createMockResponse().getData().getBrand().getCompanies()

        when:
        def result = integrationService.companiesToProducts(companies)

        then:
        result.get(0).getFinancialInstitution() == 'Big Bank'
        result.get(0).getName() == 'Easy Loan'
        result.get(0).getRateIndexer() == 'Indexer'
        result.get(0).getMinInterestRate() == 1
        result.get(0).getMaxInterestRate() == 2
    }

    def cleanupSpec() {
        mockWebServer.shutdown()
    }

    Response createMockResponse() {
        def interestRates = Arrays.asList(new InterestRate('Indexer', 'Rate', null, '0.01', '0.02'))
        def personalLoans = Arrays.asList(new PersonalLoan('Easy Loan', null, interestRates, null, 'T&C'))
        def companies = Arrays.asList(new Company('1234', 'Big Bank', 'url', personalLoans))
        def brand = new Brand('Brand', companies)
        def response = new Response(new Data(brand))
        return response
    }

    class Response {
        Data data

        Response(Data data) {
            this.data = data
        }

        Data getData() {
            return data
        }

        void setData(Data data) {
            this.data = data
        }
    }

    class Data {
        Brand brand

        Data(Brand brand) {
            this.brand = brand
        }

        Brand getBrand() {
            return brand
        }

        void setBrand(Brand brand) {
            this.brand = brand
        }
    }
}
