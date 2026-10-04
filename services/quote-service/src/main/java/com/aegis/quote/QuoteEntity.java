package com.aegis.quote;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "quote")
public class QuoteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "quote_ref", nullable = false, unique = true, length = 40)
    private String quoteRef;

    @Column(name = "product_code", nullable = false, length = 30)
    private String productCode;

    @Column(name = "applicant_name", nullable = false, length = 150)
    private String applicantName;

    @Column(name = "applicant_email", nullable = false, length = 190)
    private String applicantEmail;

    @Column(name = "premium_amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal premiumAmount;

    @Column(name = "currency", nullable = false, length = 3, columnDefinition = "char(3)")
    private String currency;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private QuoteStatus status;

    @Column(name = "valid_until", nullable = false)
    private Instant validUntil;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Version
    @Column(name = "version", nullable = false)
    private Long version;

    protected QuoteEntity(){
        // JPA
    }

    public QuoteEntity(String quoteRef, String productCode, String applicantName, String applicantEmail,
                       BigDecimal premiumAmount, String currency, QuoteStatus status, Instant validUntil) {
        this.quoteRef = quoteRef;
        this.productCode = productCode;
        this.applicantName = applicantName;
        this.applicantEmail = applicantEmail;
        this.premiumAmount = premiumAmount;
        this.currency = currency;
        this.status = status;
        this.validUntil = validUntil;
    }

    @PrePersist
    void onCreate(){
        Instant now = Instant.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    void onUpdate(){
        this.updatedAt = Instant.now();
    }

    public Long getId() {
        return id;
    }

    public String getQuoteRef() {
        return quoteRef;
    }

    public String getProductCode() {
        return productCode;
    }

    public String getApplicantName() {
        return applicantName;
    }

    public String getApplicantEmail() {
        return applicantEmail;
    }

    public BigDecimal getPremiumAmount() {
        return premiumAmount;
    }

    public String getCurrency() {
        return currency;
    }

    public QuoteStatus getStatus() {
        return status;
    }

    public Instant getValidUntil() {
        return validUntil;
    }



}
