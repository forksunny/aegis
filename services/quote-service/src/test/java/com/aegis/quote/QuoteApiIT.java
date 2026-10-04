package com.aegis.quote;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MySQLContainer;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;

import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;


@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
public class QuoteApiIT {

    @Container
    @ServiceConnection
    static final MySQLContainer<?> MYSQL = new MySQLContainer<>("mysql:8.4");

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void createsQuoteAndPriceIt() throws Exception {
        String body = """
                {
                    "productCode": "AUTO_NONSTD",
                    "applicantName": "Sunny",
                    "applicantEmail": "s@example.com",
                    "applicantAge": 23,
                    "priorClaims": 1
                }
                """;

        mockMvc.perform(post("/api/v1/quotes").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("QUOTED"))
                .andExpect(jsonPath("$.premiumAmount").value(3888.00));
    }

    @Test
    public void rejectUnknownProduct() throws Exception {
        String body = """
                {
                  "productCode": "SPACESHIP",
                  "applicantName": "Sunny",
                  "applicantEmail": "s@example.com",
                  "applicantAge": 40,
                  "priorClaims": 0
                }
                """;

        mockMvc.perform(post("/api/v1/quotes").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void returnsNotFoundForUnknownRef() throws Exception {
        mockMvc.perform(get("/api/v1/quotes/QT-DOES-NOT-EXIST"))
                .andExpect(status().isNotFound());
    }

}
