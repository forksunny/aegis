package com.aegis.policy;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/policies")
public class PolicyController {

    private final PolicyService policyService;

    public PolicyController(PolicyService policyService) {
        this.policyService = policyService;
    }

    @PostMapping
    public ResponseEntity<PolicyResponse> bind(@Valid @RequestBody BindPolicyRequest request) {
        BindResult result = policyService.bind(request);
        if (!result.created()) {
            return ResponseEntity.ok(result.policy());
        }
        URI location = UriComponentsBuilder.fromPath("/api/v1/policies/{policyNumber}")
                .buildAndExpand(result.policy().policyNumber())
                .toUri();
        return ResponseEntity.created(location).body(result.policy());
    }

    @GetMapping("/{policyNumber}")
    public PolicyResponse get(@PathVariable String policyNumber) {
        return policyService.findByPolicyNumber(policyNumber);
    }
}
