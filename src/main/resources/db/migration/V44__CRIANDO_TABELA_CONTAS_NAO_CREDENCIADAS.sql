CREATE TABLE if not exists erros (
	id SERIAL PRIMARY KEY,
	acao_Id int NOT NULL ,
	dh_created_at TIMESTAMP,
	erro VARCHAR(255),
	tipo VARCHAR(1),
	qtd_tentativas int, 
	price float8,
	loss float8,
	gain float8,
	contratos int 
);