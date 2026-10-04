package com.aegis.policy;

import jakarta.validation.constraints.NotBlank;

public record BindPolicyRequest(@NotBlank String quoteRef) {
}
