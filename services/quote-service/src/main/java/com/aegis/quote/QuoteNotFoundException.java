package com.aegis.quote;

public class QuoteNotFoundException extends RuntimeException {
    public QuoteNotFoundException(String quoteRef){
        super("Quote not found: " + quoteRef);
    }
}
