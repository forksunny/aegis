package com.aegis.policy;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PolicyRepository extends JpaRepository<PolicyEntity, Long> {
    Optional<PolicyEntity> findByPolicyNumber(String policyNumber);
    Optional<PolicyEntity> findByQuoteRef(String quoteRef);
}
