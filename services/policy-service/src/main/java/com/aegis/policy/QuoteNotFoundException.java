package com.aegis.policy;

public class QuoteNotFoundException extends RuntimeException {
    public QuoteNotFoundException(String quoteRef) {
        super("Quote not found: " + quoteRef);
    }
}
