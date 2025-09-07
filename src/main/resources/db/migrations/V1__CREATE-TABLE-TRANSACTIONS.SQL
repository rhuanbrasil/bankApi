CREATE TABLE transactions(
    id Serial primary key,
    transactionId Serial UNIQUE not null,
    type TEXT NOT NULL,
    amount FLOAT NOT NULL,
    timestamp TIMESTAMP not null,
)