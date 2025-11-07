package com.example.mortgage;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MortgageControllerIT {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void getInterestRates_returnsOk() {
        ResponseEntity<String> resp = restTemplate.getForEntity("http://localhost:" + port + "/api/interest-rates", String.class);
        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void mortgageCheck_returnsEligible() {
        String json = """
          {
            "income": 50000,
            "maturityPeriod": 20,
            "loanValue": 150000,
            "homeValue": 250000
          }
        """;

        ResponseEntity<String> resp = restTemplate.postForEntity(
                "http://localhost:" + port + "/api/mortgage-check",
                new HttpEntity<>(json, new HttpHeaders() {{
                    setContentType(MediaType.APPLICATION_JSON);
                }}),
                String.class);

        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(resp.getBody()).contains("Eligible");
    }

    @Test
    void mortgageCheck_WhenIncomeIsNull() {
        String json = """
          {
            "income": null,
            "maturityPeriod": 20,
            "loanValue": 150000,
            "homeValue": 250000
          }
        """;

        ResponseEntity<String> resp = restTemplate.postForEntity(
                "http://localhost:" + port + "/api/mortgage-check",
                new HttpEntity<>(json, new HttpHeaders() {{
                    setContentType(MediaType.APPLICATION_JSON);
                }}),
                String.class);

        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(resp.getBody()).contains("VALIDATION_ERROR");
    }

    @Test
    void mortgageCheck_WhenIncomeIsLessThanHomeValue() {
        String json = """
          {
            "income": 20000,
            "maturityPeriod": 20,
            "loanValue": 150000,
            "homeValue": 250000
          }
        """;

        ResponseEntity<String> resp = restTemplate.postForEntity(
                "http://localhost:" + port + "/api/mortgage-check",
                new HttpEntity<>(json, new HttpHeaders() {{
                    setContentType(MediaType.APPLICATION_JSON);
                }}),
                String.class);

        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(resp.getBody()).contains("Loan exceeds 4x income");
    }
}