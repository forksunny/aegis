package com.aegis.policy;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Service
public class PolicyService {

    private final PolicyRepository policyRepository;
    private final QuoteClient quoteClient;
    private final Clock clock;

    public PolicyService(PolicyRepository policyRepository, QuoteClient quoteClient, Clock clock) {
        this.policyRepository = policyRepository;
        this.quoteClient = quoteClient;
        this.clock = clock;
    }

    @Transactional
    public BindResult bind(BindPolicyRequest request){
        String quoteRef = request.quoteRef();

        Optional<PolicyEntity> existing = policyRepository.findByQuoteRef(quoteRef);
        if (existing.isPresent()) {
            return new BindResult(PolicyResponse.from(existing.get()), false);
        }

        QuoteSnapshot quote = quoteClient.fetchQuote(quoteRef)
                .orElseThrow(() -> new QuoteNotFoundException(quoteRef));

        if (!"QUOTED".equals(quote.status())) {
            throw new QuoteNotBindableException(quoteRef, "status is " + quote.status());
        }
        if (quote.validUntil().isBefore(Instant.now(clock))) {
            throw new QuoteNotBindableException(quoteRef, "quote expired on " + quote.validUntil());
        }

        LocalDate effectiveFrom = LocalDate.now(clock);
        PolicyEntity policy = new PolicyEntity(
                newPolicyNumber(),
                quoteRef,
                quote.productCode(),
                PolicyStatus.BOUND,
                quote.premiumAmount(),
                quote.currency(),
                effectiveFrom,
                effectiveFrom.plusYears(1));

        try {
            return new BindResult(PolicyResponse.from(policyRepository.saveAndFlush(policy)), true);
        } catch (DataIntegrityViolationException exception) {
            return policyRepository.findByQuoteRef(quoteRef)
                    .map(found -> new BindResult(PolicyResponse.from(found), false))
                    .orElseThrow(() -> exception);
        }
    }

    @Transactional(readOnly = true)
    public PolicyResponse findByPolicyNumber(String policyNumber) {
        return policyRepository.findByPolicyNumber(policyNumber)
                .map(PolicyResponse::from)
                .orElseThrow(() -> new PolicyNotFoundException(policyNumber));
    }

    private static String newPolicyNumber() {
        return "POL-" + UUID.randomUUID().toString().substring(0, 12).toUpperCase();
    }
}
