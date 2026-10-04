package com.aegis.quote;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Service
public class QuoteService {
    private static final Duration QUOTE_VALIDITY = Duration.ofDays(30);

    private final QuoteRepository quoteRepository;
    private final RatingEngine ratingEngine;

    public QuoteService(QuoteRepository quoteRepository, RatingEngine ratingEngine) {
        this.quoteRepository = quoteRepository;
        this.ratingEngine = ratingEngine;
    }

    @Transactional
    public QuoteResponse createQuote(CreateQuoteRequest request){
        BigDecimal premium = ratingEngine.rate(request);
        Instant now = Instant.now();

        QuoteEntity quote = new QuoteEntity(
                newQuoteRef(),
                request.productCode(),
                request.applicantName(),
                request.applicantEmail(),
                premium,
                "USD",
                QuoteStatus.QUOTED,
                now.plus(QUOTE_VALIDITY)
        );

        QuoteEntity savedQuote = quoteRepository.save(quote);
        return QuoteResponse.from(savedQuote);
    }

    @Transactional(readOnly = true)
    public QuoteResponse findByRef(String quoteRef) {
        return quoteRepository.findByQuoteRef(quoteRef)
                .map(QuoteResponse::from)
                .orElseThrow(() -> new QuoteNotFoundException(quoteRef));
    }

    private static String newQuoteRef() {
        return "QT-" + UUID.randomUUID().toString().substring(0, 12).toUpperCase();
    }
}
