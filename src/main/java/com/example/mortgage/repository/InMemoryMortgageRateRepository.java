package com.example.mortgage.repository;

import com.example.mortgage.domain.model.MortgageRate;
import org.springframework.stereotype.Repository;
import jakarta.annotation.PostConstruct;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryMortgageRateRepository implements MortgageRateRepository {

    private final Map<Integer, MortgageRate> rates = new ConcurrentHashMap<>();

    @PostConstruct
    void seed() {
        rates.put(10, new MortgageRate(10, new BigDecimal("3.40"), Instant.now()));
        rates.put(20, new MortgageRate(20, new BigDecimal("3.75"), Instant.now()));
        rates.put(30, new MortgageRate(30, new BigDecimal("4.05"), Instant.now()));
    }

    @Override
    public Optional<MortgageRate> findByMaturityPeriod(Integer maturityPeriod) {
        return Optional.ofNullable(rates.get(maturityPeriod));
    }

    @Override
    public List<MortgageRate> findAll() {
        return new ArrayList<>(rates.values());
    }
}
