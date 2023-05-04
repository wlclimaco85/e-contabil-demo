
CREATE TABLE if not exists estrategias_por_acao (
	id SERIAL PRIMARY KEY,
	acaoId int NOT NULL ,
	estrategiaId int NOT NULL ,
	dh_created_at TIMESTAMP,
	valorcompra float8,
	valorcompraFinal float8,
	status VARCHAR(1) NOT NULL DEFAULT 'A'
);


CREATE TABLE if not exists estrategias (
	id SERIAL PRIMARY KEY,
	estrategia VARCHAR(50) NOT NULL,
	descricao VARCHAR(50) ,
	dh_created_at TIMESTAMP,
	margemAcerto float8,
	qtdordens int,
	qtdgain int,
	qtdloss int,
	status VARCHAR(1) NOT NULL DEFAULT 'A'
);

CREATE TABLE if not exists ordens (
	id SERIAL PRIMARY KEY,
	acaoId int NOT NULL ,
	tipo VARCHAR(1) NOT NULL DEFAULT 'C',
	contratos INT,
	dh_created_at TIMESTAMP,
	status VARCHAR(1) NOT NULL DEFAULT 'A',
	valorCompra float8,
	valorVenda float8,
	loss float8,
	gain float8,
	ambiente int NOT NULL ,
	dh_compra_at TIMESTAMP,
	dh_venda_at TIMESTAMP
);

