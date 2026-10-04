package com.aegis.quote;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface QuoteRepository extends JpaRepository<QuoteEntity, Long> {
    Optional<QuoteEntity> findByQuoteRef(String quoteRef);
}
