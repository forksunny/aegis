package com.aegis.quote;

import java.math.BigDecimal;
import java.time.Instant;

public record QuoteResponse(
        String quoteRef,
        String productCode,
        BigDecimal premiumAmount,
        String currency,
        QuoteStatus status,
        Instant validUntil
) {
    static QuoteResponse from(QuoteEntity entity){
        return new QuoteResponse(
                entity.getQuoteRef(),
                entity.getProductCode(),
                entity.getPremiumAmount(),
                entity.getCurrency(),
                entity.getStatus(),
                entity.getValidUntil());
    }
}