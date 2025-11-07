package com.example.mortgage.domain.rule;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

class IncomeMultipleRuleTest {

    @Test void passes_when_loan_within_4x_income() {
        var rule = new IncomeMultipleRule();
        assertNull(rule.validate(new BigDecimal("50000"), new BigDecimal("150000"), null));
    }

    @Test void fails_when_loan_exceeds_4x_income() {
        var rule = new IncomeMultipleRule();
        assertEquals("Loan exceeds 4x income", rule.validate(new BigDecimal("50000"), new BigDecimal("250001"), null));
    }
}
