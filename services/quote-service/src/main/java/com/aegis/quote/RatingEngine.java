package com.aegis.quote;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

@Component
public class RatingEngine {
    static final String RULE_VERSION = "2026.10.1";

    private static final Map<String, BigDecimal> BASE_RATES = Map.of(
            "AUTO_STD", new BigDecimal("1200.00"),
            "AUTO_NONSTD", new BigDecimal("2400.00"),
            "HOME_STD", new BigDecimal("900.00"));

    public BigDecimal rate(CreateQuoteRequest request){
        BigDecimal base = BASE_RATES.get(request.productCode());
        if(base == null){
            throw new UnknownProductException(request.productCode());
        }

        BigDecimal ageFactor = request.applicantAge() < 25 ? new BigDecimal("1.35") : BigDecimal.ONE;

        BigDecimal claimsFactor = BigDecimal.ONE
                .add(new BigDecimal("0.20").multiply(BigDecimal.valueOf(request.priorClaims())));

        return base.multiply(ageFactor)
                .multiply(claimsFactor)
                .setScale(2, RoundingMode.HALF_UP);
    }
}
