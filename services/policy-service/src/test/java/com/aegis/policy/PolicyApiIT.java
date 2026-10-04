package com.aegis.policy;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
public class PolicyApiIT {

    @Container
    @ServiceConnection
    static final MySQLContainer<?> MYSQL = new MySQLContainer<>("mysql:8.4");

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private QuoteClient quoteClient;

    @BeforeEach
    void stubQuoteService() {
        when(quoteClient.fetchQuote(any())).thenReturn(Optional.of(new QuoteSnapshot(
                "QT-IT-001",
                "AUTO_STD",
                new BigDecimal("1200.00"),
                "USD",
                "QUOTED",
                Instant.now().plus(30, ChronoUnit.DAYS))));
    }

    @Test
    void bindsQuoteThenReturnsSamePolicyOnRetry() throws Exception {
        String body = """
                { "quoteRef": "QT-IT-001" }
                """;

        String policyNumber = mockMvc.perform(post("/api/v1/policies")
                        .contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("BOUND"))
                .andExpect(jsonPath("$.premiumAmount").value(1200.00))
                .andReturn()
                .getResponse()
                .getContentAsString()
                .replaceAll(".*\"policyNumber\":\"([^\"]+)\".*", "$1");

        mockMvc.perform(post("/api/v1/policies")
                        .contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.policyNumber").value(policyNumber));
    }

    @Test
    void returnsUnprocessableWhenQuoteDoesNotExist() throws Exception {
        when(quoteClient.fetchQuote("QT-MISSING")).thenReturn(Optional.empty());

        String body = """
                { "quoteRef": "QT-MISSING" }
                """;

        mockMvc.perform(post("/api/v1/policies")
                        .contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void returnsNotFoundForUnknownPolicyNumber() throws Exception {
        mockMvc.perform(get("/api/v1/policies/POL-DOES-NOT-EXIST"))
                .andExpect(status().isNotFound());
    }
}
