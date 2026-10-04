CREATE TABLE policy (
        id             BIGINT         NOT NULL AUTO_INCREMENT,
        policy_number  VARCHAR(40)    NOT NULL,
        quote_ref      VARCHAR(40)    NOT NULL,
        product_code   VARCHAR(30)    NOT NULL,
        status         VARCHAR(20)    NOT NULL,
        premium_amount DECIMAL(15, 2) NOT NULL,
        currency       CHAR(3)        NOT NULL DEFAULT 'USD',
        effective_from DATE           NOT NULL,
        effective_to   DATE           NOT NULL,
        created_at     DATETIME(6)    NOT NULL,
        updated_at     DATETIME(6)    NOT NULL,
        version        BIGINT         NOT NULL DEFAULT 0,
        PRIMARY KEY (id),
        UNIQUE KEY uk_policy_number (policy_number),
        UNIQUE KEY uk_policy_quote_ref (quote_ref),
        KEY idx_policy_status (status)
) ENGINE = InnoDB;