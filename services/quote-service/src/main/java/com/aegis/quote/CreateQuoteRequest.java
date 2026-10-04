package com.aegis.quote;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateQuoteRequest(
        @NotBlank String productCode,
        @NotBlank String applicantName,
        @NotBlank @Email String applicantEmail,
        @NotNull @Min(18) Integer applicantAge,
        @NotNull @Min(0) Integer priorClaims
) {
}
