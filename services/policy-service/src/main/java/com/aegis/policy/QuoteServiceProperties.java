package com.aegis.policy;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@ConfigurationProperties(prefix = "aegis.quote-service")
public record QuoteServiceProperties(
        String baseUrl,
        Duration connectTimeout,
        Duration readTimeout
) {
}
