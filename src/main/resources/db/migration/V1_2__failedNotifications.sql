CREATE TABLE IF NOT EXISTS failed_notifications (
    id SERIAL PRIMARY KEY,
    transaction_id INT NOT NULL,
    email VARCHAR(100) NOT NULL,
    message VARCHAR(255) NOT NULL,
    created_at TIMESTAMP,
    FOREIGN KEY (transaction_id) REFERENCES transactions(id)
);