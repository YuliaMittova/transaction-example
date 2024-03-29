-- there are 2 tables: account and transaction connected by foreign key

CREATE TABLE account (
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    ACCOUNT_NAME VARCHAR(150) NULL,
    CURRENT_AMOUNT DOUBLE PRECISION NOT NULL;
    KEY (id)
) ENGINE = MYISAM;

CREATE TABLE transaction (
    id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    ACCOUNT BIGINT NULL,
    TYPE VARCHAR(30) NULL, -- can be DEBIT/CREDIT
    AMOUNT BIGINT NOT NULL,
    EXECUTED_AT datetime NULL,
    KEY (id)
) ENGINE = MYISAM;

--id | account_id | type (DEBIT/CREDIT) | amount (DOUBLE) | executedAt (INSTANT)
ALTER TABLE transaction ADD CONSTRAINT FK_TRANSACTION_ON_ACCOUNT FOREIGN KEY (ACCOUNT) REFERENCES account (ID);