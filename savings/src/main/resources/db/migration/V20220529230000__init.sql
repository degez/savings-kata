CREATE SEQUENCE seq_balance
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


CREATE TABLE IF NOT EXISTS balance
(
    id                      BIGINT    NOT NULL PRIMARY KEY,
    account                 VARCHAR(255)   NOT NULL UNIQUE,
    amount                  NUMERIC   NOT NULL,
    created_on              TIMESTAMP DEFAULT now(),
    modified_on             TIMESTAMP DEFAULT now()
    );

CREATE INDEX IF NOT EXISTS balance_id_idx ON balance(id);
CREATE INDEX IF NOT EXISTS balance_account_idx on balance(account);