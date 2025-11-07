package com.example.mortgage.domain.rule;

import java.math.BigDecimal;

public interface MortgageRule {
    /** Return null if valid; otherwise return a human-readable reason. */
    String validate(BigDecimal income, BigDecimal loanValue, BigDecimal homeValue);
}
