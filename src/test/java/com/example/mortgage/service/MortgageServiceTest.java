package com.example.mortgage.service;

import com.example.mortgage.api.dto.MortgageCheckResponse;
import com.example.mortgage.domain.model.MortgageRate;
import com.example.mortgage.domain.rule.MortgageRuleEngine;
import com.example.mortgage.repository.MortgageRateRepository;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MortgageServiceTest {

    @Test void eligible_when_rules_pass_and_rate_exists() {
        var repo = mock(MortgageRateRepository.class);
        var rules = mock(MortgageRuleEngine.class);
        when(rules.firstViolation(any(), any(), any())).thenReturn(null);
        when(repo.findByMaturityPeriod(20)).thenReturn(Optional.of(new MortgageRate(20, new BigDecimal("3.75"), Instant.now())));
        var svc = new MortgageService(repo, rules);
        MortgageCheckResponse resp = svc.check(new BigDecimal("50000"), 20, new BigDecimal("150000"), new BigDecimal("250000"));
        assertTrue(resp.eligible());
        assertEquals("Eligible", resp.reason());
        assertEquals(new BigDecimal("3.75"), resp.interestRate());
    }

    @Test void not_found_when_rate_missing() {
        var repo = mock(MortgageRateRepository.class);
        var rules = mock(MortgageRuleEngine.class);
        when(rules.firstViolation(any(), any(), any())).thenReturn(null);
        when(repo.findByMaturityPeriod(99)).thenReturn(Optional.empty());
        var svc = new MortgageService(repo, rules);
        assertThrows(java.util.NoSuchElementException.class,
                () -> svc.check(new BigDecimal("50000"), 99, new BigDecimal("150000"), new BigDecimal("250000")));
    }
}
