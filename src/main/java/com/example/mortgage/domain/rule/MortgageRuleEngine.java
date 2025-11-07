package com.example.mortgage.domain.rule;

import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;

@Service
public class MortgageRuleEngine {
    private final List<MortgageRule> rules;
    public MortgageRuleEngine(List<MortgageRule> rules) { this.rules = rules; }

    public String firstViolation(BigDecimal income, BigDecimal loanValue, BigDecimal homeValue) {
        for (var rule : rules) {
            var reason = rule.validate(income, loanValue, homeValue);
            if (reason != null) return reason;
        }
        return null;
    }
}
