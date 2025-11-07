package com.example.mortgage.domain.rule;

import org.springframework.stereotype.Component;
import java.math.BigDecimal;

@Component
public class HomeValueRule implements MortgageRule {

    @Override
    public String validate(BigDecimal income, BigDecimal loanValue, BigDecimal homeValue) {
        if (homeValue == null || loanValue == null) return "Home value/loan required";
        return loanValue.compareTo(homeValue) <= 0 ? null : "Loan exceeds home value";
    }
}
