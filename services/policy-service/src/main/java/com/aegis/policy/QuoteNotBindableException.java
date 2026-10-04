package com.aegis.policy;

public class QuoteNotBindableException extends RuntimeException {
    public QuoteNotBindableException(String quoteRef, String reason) {
        super("Quote " + quoteRef + " cannot be bound: " + reason);
    }
}
