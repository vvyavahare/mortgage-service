package com.example.mortgage.repository;

import com.example.mortgage.domain.model.MortgageRate;
import java.util.List;
import java.util.Optional;

public interface MortgageRateRepository {
    Optional<MortgageRate> findByMaturityPeriod(Integer maturityPeriod);
    List<MortgageRate> findAll();
}
