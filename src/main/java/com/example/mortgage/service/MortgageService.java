package com.example.mortgage.service;

import com.example.mortgage.api.dto.MortgageCheckResponse;
import com.example.mortgage.domain.model.MortgageRate;
import com.example.mortgage.domain.rule.MortgageRuleEngine;
import com.example.mortgage.repository.MortgageRateRepository;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class MortgageService {

    private final MortgageRateRepository rateRepo;
    private final MortgageRuleEngine rules;

    public MortgageService(MortgageRateRepository rateRepo, MortgageRuleEngine rules) {
        this.rateRepo = rateRepo; this.rules = rules;
    }

    public Optional<MortgageRate> getRate(Integer maturityPeriod) {
        return rateRepo.findByMaturityPeriod(maturityPeriod);
    }

    public MortgageCheckResponse check(BigDecimal income, Integer maturityPeriod,
                                       BigDecimal loanValue, BigDecimal homeValue) {
        var violation = rules.firstViolation(income, loanValue, homeValue);
        if (violation != null) return new MortgageCheckResponse(false, violation, null);

        var rate = getRate(maturityPeriod)
                .orElseThrow(() -> new NoSuchElementException("Rate not found for maturity=" + maturityPeriod));

        // Simplified monthly cost calculation: (loan * (r/100) / 12)  (interest-only)
        var monthly = loanValue.multiply(rate.interestRate().divide(new BigDecimal("100"))).divide(new BigDecimal("12"));
        return new MortgageCheckResponse(true, "Eligible", rate.interestRate());
    }
}
