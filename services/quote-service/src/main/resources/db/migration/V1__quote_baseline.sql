CREATE TABLE quote (
       id              BIGINT         NOT NULL AUTO_INCREMENT,
       quote_ref       VARCHAR(40)    NOT NULL,
       product_code    VARCHAR(30)    NOT NULL,
       applicant_name  VARCHAR(150)   NOT NULL,
       applicant_email VARCHAR(190)   NOT NULL,
       premium_amount  DECIMAL(15, 2) NOT NULL,
       currency        CHAR(3)        NOT NULL DEFAULT 'USD',
       status          VARCHAR(20)    NOT NULL,
       valid_until     DATETIME(6)    NOT NULL,
       created_at      DATETIME(6)    NOT NULL,
       updated_at      DATETIME(6)    NOT NULL,
       version         BIGINT         NOT NULL DEFAULT 0,
       PRIMARY KEY (id),
       UNIQUE KEY uk_quote_ref (quote_ref),
       KEY idx_quote_status_valid (status, valid_until)
) ENGINE = InnoDB;