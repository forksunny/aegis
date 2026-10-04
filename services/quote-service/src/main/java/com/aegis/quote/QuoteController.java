package com.aegis.quote;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/quotes")
public class QuoteController {
    private final QuoteService quoteService;

    public QuoteController(QuoteService quoteService) {
        this.quoteService = quoteService;
    }

    @PostMapping
    public ResponseEntity<QuoteResponse> create(@Valid @RequestBody CreateQuoteRequest request){
        QuoteResponse response = quoteService.createQuote(request);
        URI location = UriComponentsBuilder.fromPath("/api/v1/quotes/{ref}")
                .buildAndExpand(response.quoteRef())
                .toUri();

        return  ResponseEntity.created(location).body(response);
    }

    @GetMapping("/{quoteRef}")
    public QuoteResponse get(@PathVariable String quoteRef) {
        return quoteService.findByRef(quoteRef);
    }
}
