package com.aegis.quote;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class RatingEngineTest {

    public static RatingEngine ratingEngine = new RatingEngine();

    @Test
    public void appliesBaseRateWithNoLoadings(){
        BigDecimal premium = ratingEngine.rate(request("AUTO_STD", 40, 0));
        assertEquals(new BigDecimal("1200.00"), premium);
    }

    @Test
    public void loadsYoungDriverByThirtyFivePercent() {
        BigDecimal premium = ratingEngine.rate(request("AUTO_STD", 24, 0));
        assertEquals(new BigDecimal("1620.00"), premium);
    }

    @Test
    public void doesNotLoadDriverAtExactlyTwentyFive() {
        BigDecimal premium = ratingEngine.rate(request("AUTO_STD", 25, 0));
        assertEquals(new BigDecimal("1200.00"), premium);
    }

    @Test
    public void loadsTwentyPercentPerPriorClaim() {
        BigDecimal premium = ratingEngine.rate(request("AUTO_STD", 40, 2));
        assertEquals(new BigDecimal("1680.00"), premium);
    }

    @Test
    public void compoundsAgeAndClaimLoadings() {
        BigDecimal premium = ratingEngine.rate(request("AUTO_NONSTD", 23, 1));
        assertEquals(new BigDecimal("3888.00"), premium);
    }

    @Test
    public void ratesDeterministically() {
        CreateQuoteRequest request = request("AUTO_NONSTD", 23, 1);
        assertEquals(ratingEngine.rate(request), ratingEngine.rate(request));
    }

    @Test
    public void rejectsUnknownProduct() {
        assertThrows(UnknownProductException.class, () -> ratingEngine.rate(request("SPACESHIP", 40, 0)));
    }

    private static CreateQuoteRequest request(String productCode, int age, int priorClaims) {
        return new CreateQuoteRequest(productCode, "Sunny", "s@example.com", age, priorClaims);
    }
}
