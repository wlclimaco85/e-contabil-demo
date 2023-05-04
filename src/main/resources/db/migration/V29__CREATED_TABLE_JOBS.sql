create table if not exists  jobs  (
   id SERIAL PRIMARY KEY,
   data_inicio TIMESTAMP,
   data_final TIMESTAMP,
   data_prox_exec TIMESTAMP,
   tipo INT,
   error VARCHAR,
   qtd_registro INT,
   status INT
);
