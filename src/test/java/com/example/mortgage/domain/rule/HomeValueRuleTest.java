package com.example.mortgage.domain.rule;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

class HomeValueRuleTest {

    @Test void passes_when_loan_leq_home_value() {
        var rule = new HomeValueRule();
        assertNull(rule.validate(null, new BigDecimal("200000"), new BigDecimal("250000")));
    }

    @Test void fails_when_loan_gt_home_value() {
        var rule = new HomeValueRule();
        assertEquals("Loan exceeds home value", rule.validate(null, new BigDecimal("260000"), new BigDecimal("250000")));
    }
}
