package com.aegis.policy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.math.BigDecimal;
import java.time.Instant;

@JsonIgnoreProperties(ignoreUnknown = true)
public record QuoteSnapshot(
        String quoteRef,
        String productCode,
        BigDecimal premiumAmount,
        String currency,
        String status,
        Instant validUntil
) {
}
