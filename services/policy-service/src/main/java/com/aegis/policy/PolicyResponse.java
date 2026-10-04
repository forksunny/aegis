package com.aegis.policy;

import java.math.BigDecimal;
import java.time.LocalDate;

public record PolicyResponse(
        String policyNumber,
        String quoteRef,
        String productCode,
        PolicyStatus status,
        BigDecimal premiumAmount,
        String currency,
        LocalDate effectiveFrom,
        LocalDate effectiveTo
) {
    static PolicyResponse from(PolicyEntity entity) {
        return new PolicyResponse(
                entity.getPolicyNumber(),
                entity.getQuoteRef(),
                entity.getProductCode(),
                entity.getStatus(),
                entity.getPremiumAmount(),
                entity.getCurrency(),
                entity.getEffectiveFrom(),
                entity.getEffectiveTo());
    }
}
