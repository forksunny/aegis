package com.aegis.policy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PolicyServiceTest {
    private static final Instant NOW = Instant.parse("2026-10-04T00:00:00Z");

    @Mock
    private PolicyRepository policyRepository;

    @Mock
    private QuoteClient quoteClient;

    private PolicyService policyService;

    @BeforeEach
    void setUp() {
        Clock fixedClock = Clock.fixed(NOW, ZoneOffset.UTC);
        policyService = new PolicyService(policyRepository, quoteClient, fixedClock);
    }

    @Test
    void bindsValidQuote() {
        when(policyRepository.findByQuoteRef("QT-1")).thenReturn(Optional.empty());
        when(quoteClient.fetchQuote("QT-1")).thenReturn(Optional.of(quote("QUOTED", NOW.plusSeconds(86400))));
        when(policyRepository.saveAndFlush(any(PolicyEntity.class))).thenAnswer(call -> call.getArgument(0));

        BindResult result = policyService.bind(new BindPolicyRequest("QT-1"));

        assertTrue(result.created());
        assertEquals(PolicyStatus.BOUND, result.policy().status());
        assertEquals(new BigDecimal("1200.00"), result.policy().premiumAmount());
        assertEquals(LocalDate.of(2026, 10, 4), result.policy().effectiveFrom());
        assertEquals(LocalDate.of(2027, 10, 4), result.policy().effectiveTo());
    }

    @Test
    void returnsExistingPolicyWithoutCallingQuoteService() {
        PolicyEntity existing = new PolicyEntity("POL-1", "QT-1", "AUTO_STD", PolicyStatus.BOUND,
                new BigDecimal("1200.00"), "USD", LocalDate.of(2026, 1, 1), LocalDate.of(2027, 1, 1));
        when(policyRepository.findByQuoteRef("QT-1")).thenReturn(Optional.of(existing));

        BindResult result = policyService.bind(new BindPolicyRequest("QT-1"));

        assertFalse(result.created());
        assertEquals("POL-1", result.policy().policyNumber());
        verify(quoteClient, never()).fetchQuote(any());
    }

    @Test
    void rejectsExpiredQuote() {
        when(policyRepository.findByQuoteRef("QT-1")).thenReturn(Optional.empty());
        when(quoteClient.fetchQuote("QT-1")).thenReturn(Optional.of(quote("QUOTED", NOW.minusSeconds(1))));

        assertThrows(QuoteNotBindableException.class, () -> policyService.bind(new BindPolicyRequest("QT-1")));
        verify(policyRepository, never()).saveAndFlush(any());
    }

    @Test
    void rejectsQuoteThatIsNotInQuotedStatus() {
        when(policyRepository.findByQuoteRef("QT-1")).thenReturn(Optional.empty());
        when(quoteClient.fetchQuote("QT-1")).thenReturn(Optional.of(quote("EXPIRED", NOW.plusSeconds(86400))));

        assertThrows(QuoteNotBindableException.class, () -> policyService.bind(new BindPolicyRequest("QT-1")));
    }

    @Test
    void rejectsMissingQuote() {
        when(policyRepository.findByQuoteRef("QT-1")).thenReturn(Optional.empty());
        when(quoteClient.fetchQuote("QT-1")).thenReturn(Optional.empty());

        assertThrows(QuoteNotFoundException.class, () -> policyService.bind(new BindPolicyRequest("QT-1")));
    }

    private static QuoteSnapshot quote(String status, Instant validUntil) {
        return new QuoteSnapshot("QT-1", "AUTO_STD", new BigDecimal("1200.00"), "USD", status, validUntil);
    }
}
