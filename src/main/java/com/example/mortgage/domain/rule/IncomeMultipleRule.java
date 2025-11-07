package com.example.mortgage.domain.rule;

import org.springframework.stereotype.Component;
import java.math.BigDecimal;

@Component
public class IncomeMultipleRule implements MortgageRule {

    @Override
    public String validate(BigDecimal income, BigDecimal loanValue, BigDecimal homeValue) {
        if (income == null || loanValue == null) return "Income/loan required";
        var max = income.multiply(BigDecimal.valueOf(4));
        return loanValue.compareTo(max) <= 0 ? null : "Loan exceeds 4x income";
    }
}
