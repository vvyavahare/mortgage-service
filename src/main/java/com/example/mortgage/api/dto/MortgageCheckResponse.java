package com.example.mortgage.api.dto;

import java.math.BigDecimal;

public record MortgageCheckResponse(
    boolean eligible,
    String reason,
    BigDecimal interestRate
) {}
