package com.aegis.policy;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.Optional;

@Component
public class QuoteClient {

    private final RestClient restClient;

    public QuoteClient(RestClient quoteRestClient) {
        this.restClient = quoteRestClient;
    }

    public Optional<QuoteSnapshot> fetchQuote(String quoteRef) {
        try {
            return restClient.get()
                    .uri("/api/v1/quotes/{quoteRef}", quoteRef)
                    .exchange((request, response) -> {
                        if (response.getStatusCode().value() == 404) {
                            return Optional.<QuoteSnapshot>empty();
                        }
                        if (response.getStatusCode().isError()) {
                            throw new QuoteServiceUnavailableException(
                                    quoteRef, "upstream returned " + response.getStatusCode());
                        }
                        return Optional.ofNullable(response.bodyTo(QuoteSnapshot.class));
                    });
        } catch (RestClientException exception) {
            throw new QuoteServiceUnavailableException(quoteRef, exception);        }
    }

}
