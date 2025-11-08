package com.example.mortgage.service;

import com.example.mortgage.api.dto.MortgageCheckResponse;
import com.example.mortgage.domain.model.MortgageRate;
import com.example.mortgage.domain.rule.MortgageRuleEngine;
import com.example.mortgage.repository.MortgageRateRepository;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
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

    public MortgageCheckResponse check(BigDecimal income,
                                       Integer maturityPeriod,
                                       BigDecimal loanValue,
                                       BigDecimal homeValue) {

        // Step 1: Validate rules
        var violation = rules.firstViolation(income, loanValue, homeValue);
        if (violation != null) {
            return new MortgageCheckResponse(false, violation, null);
        }

        // Step 2: Get rate for the given maturity period
        var rate = getRate(maturityPeriod)
                .orElseThrow(() -> new NoSuchElementException("Rate not found for maturity=" + maturityPeriod));

        BigDecimal annualRate = rate.interestRate(); // e.g. 5.0 = 5%
        BigDecimal principal = loanValue;
        int years = maturityPeriod;

        // Step 3: Compute monthly payment using amortization formula
        BigDecimal monthlyPayment = calculateMonthlyPayment(principal, annualRate, years);

        // Step 4: Return full response
        return new MortgageCheckResponse(true, "Eligible", monthlyPayment);
    }

    /**
     * Compute monthly mortgage payment using amortization formula:
     * M = P * [ r(1+r)^n / ((1+r)^n - 1) ]
     * with proper BigDecimal handling and 0% interest case.
     */
    private BigDecimal calculateMonthlyPayment(BigDecimal principal,
                                               BigDecimal annualRate,
                                               int years) {
        final MathContext mc = new MathContext(16, RoundingMode.HALF_EVEN);

        // Convert annual rate (%) → monthly decimal
        BigDecimal monthlyRate = annualRate
                .divide(BigDecimal.valueOf(100 * 12.0), mc);

        int n = years * 12; // total months

        // Handle 0% interest special case
        if (monthlyRate.compareTo(BigDecimal.ZERO) == 0) {
            return principal.divide(BigDecimal.valueOf(n), 2, RoundingMode.HALF_EVEN);
        }

        // (1 + r)^n
        BigDecimal onePlusR = BigDecimal.ONE.add(monthlyRate, mc);
        BigDecimal pow = onePlusR.pow(n, mc);

        // r(1+r)^n / ((1+r)^n - 1)
        BigDecimal numerator = monthlyRate.multiply(pow, mc);
        BigDecimal denominator = pow.subtract(BigDecimal.ONE, mc);

        BigDecimal monthly = principal.multiply(numerator, mc)
                .divide(denominator, 2, RoundingMode.HALF_EVEN);

        return monthly;
    }

}
