create table if not exists  certificados  (
   id SERIAL PRIMARY KEY,
   idapibanco  INT,
   clientid VARCHAR NOT NULL,
   clientsecret VARCHAR NOT NULL,
   token VARCHAR NOT NULL,
   csr BYTEA NOT NULL,
   key BYTEA NOT NULL,
   crt BYTEA NOT NULL,
   p12 BYTEA NOT NULL 
);
