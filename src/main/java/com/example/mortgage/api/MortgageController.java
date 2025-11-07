package com.example.mortgage.api;

import com.example.mortgage.api.dto.MortgageCheckRequest;
import com.example.mortgage.api.dto.MortgageCheckResponse;
import com.example.mortgage.domain.model.MortgageRate;
import com.example.mortgage.service.MortgageService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api")
@Tag(name = "Mortgage Application Endpoints", description = "All the APIs needed to handle mortgage are listed here")
public class MortgageController {

    private final MortgageService service;

    public MortgageController(MortgageService service) {
        this.service = service;
    }

    @GetMapping("/interest-rates")
    public ResponseEntity<MortgageRate> getRates() {
        return ResponseEntity
                .ok(service.getRate(10).orElse(null));
    }

    @PostMapping("/mortgage-check")
    public ResponseEntity<MortgageCheckResponse> check(@Valid @RequestBody MortgageCheckRequest req) {
        return ResponseEntity
                .ok(service.check(req.income(), req.maturityPeriod(), req.loanValue(), req.homeValue()));
    }
}
