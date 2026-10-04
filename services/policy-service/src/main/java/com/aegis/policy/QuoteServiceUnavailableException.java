package com.aegis.policy;

public class QuoteServiceUnavailableException extends RuntimeException{
    public QuoteServiceUnavailableException(String quoteRef, Throwable cause){
        super("Could not reach quote-service for quote: " + quoteRef, cause);
    }
    public QuoteServiceUnavailableException(String quoteRef, String reason) {
        super("quote-service failed for quote " + quoteRef + ": " + reason);
    }
}
