package com.example.mortgage.api;

import com.example.mortgage.api.dto.MortgageCheckRequest;
import com.example.mortgage.api.dto.MortgageCheckResponse;
import com.example.mortgage.service.MortgageService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.math.BigDecimal;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MortgageController.class)
class MortgageControllerTest {

    @Autowired MockMvc mvc;
    @Autowired ObjectMapper om;
    @MockBean MortgageService service;

    @Test void returns_200_on_valid_request() throws Exception {
        when(service.check(any(), any(), any(), any()))
                .thenReturn(new MortgageCheckResponse(true, "Eligible", new BigDecimal("3.40")));
        var req = new MortgageCheckRequest(new BigDecimal("50000"), 20, new BigDecimal("150000"), new BigDecimal("250000"));
        mvc.perform(post("/api/mortgage-check")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.eligible").value(true))
                .andExpect(jsonPath("$.interestRate").value(3.40));
    }

    @Test void returns_400_on_validation_error() throws Exception {
        var invalid = new MortgageCheckRequest(null, 20, new BigDecimal("150000"), new BigDecimal("250000"));
        mvc.perform(post("/api/mortgage-check")
                .contentType(MediaType.APPLICATION_JSON)
                .content(om.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"));
    }
}
