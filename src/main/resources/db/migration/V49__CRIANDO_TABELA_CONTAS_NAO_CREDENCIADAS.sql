
CREATE TABLE if not exists breakeven (
	id SERIAL PRIMARY KEY,
	acao_Id int NOT NULL ,
	loss_Atual float8 NOT NULL ,
	gain_Atual float8 NOT NULL ,
	dh_created_at TIMESTAMP,
	valor_Atual_Acao float8,
	status VARCHAR(1) NOT NULL DEFAULT 'A'
);

ALTER TABLE acoes ADD COLUMN loss_Corrente float8;
ALTER TABLE acoes ADD COLUMN gain_Corrente float8;
