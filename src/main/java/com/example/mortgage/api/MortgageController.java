package com.example.mortgage.api;

import com.example.mortgage.api.dto.*;
import com.example.mortgage.service.MortgageService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class MortgageController {

    private final MortgageService service;

    public MortgageController(MortgageService service) { this.service = service; }

    @GetMapping("/interest-rates")
    public ResponseEntity<?> getRates() {
        return ResponseEntity.ok(service.getRate(10).map(r -> r).orElse(null));
    }

    @PostMapping("/mortgage-check")
    public MortgageCheckResponse check(@Valid @RequestBody MortgageCheckRequest req) {
        return service.check(req.income(), req.maturityPeriod(), req.loanValue(), req.homeValue());
    }
}
