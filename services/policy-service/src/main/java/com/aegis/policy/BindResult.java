package com.aegis.policy;

public record BindResult(
        PolicyResponse policy, boolean created
) {
}
