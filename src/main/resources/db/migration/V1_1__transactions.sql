CREATE TABLE IF NOT EXISTS transactions (
    id SERIAL PRIMARY KEY,
    payer_id INT NOT NULL,
    payee_id INT NOT NULL,
    "value" DECIMAL(19, 2) NOT NULL,
    status VARCHAR(10) CHECK (status IN ('APPROVED', 'CANCELED')) NOT NULL,
    created_at TIMESTAMP,
    FOREIGN KEY (payer_id) REFERENCES users(id),
    FOREIGN KEY (payee_id) REFERENCES users(id)
);