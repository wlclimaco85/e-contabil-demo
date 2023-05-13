create table if not exists corretoras (
	id SERIAL PRIMARY KEY,
	status VARCHAR(1) NOT NULL DEFAULT 'P',
	saldo float8,
	usuario_Site VARCHAR(12) ,
	senha_Site VARCHAR(12) ,
	usuariomq5 VARCHAR(12) ,
	senhamq5 VARCHAR(12) ,
	usuario_Pro_Fit VARCHAR(12) ,
	senha_Pro_Fit VARCHAR(12) ,
	nome VARCHAR(100),
	banco VARCHAR(100),
	agencia VARCHAR(100),
	conta VARCHAR(100),
	ambiente INT  DEFAULT 0,
	dh_created_at TIMESTAMP,
	dh_updated_at TIMESTAMP
);

create table if not exists acoes (
	id SERIAL PRIMARY KEY,
	acao VARCHAR(20),
	status VARCHAR(1) NOT NULL DEFAULT 'P',
	lucroPreju float8,
	valorSuj float8,
	tipo VARCHAR(1) NOT NULL DEFAULT 'C',
	periodo INT,
	nome_robo VARCHAR(100),
	data TIMESTAMP,
	valoracaoatual float8,
	shortname  VARCHAR(200),
	level INT,
	loss float8,
    gain float8,
	mudou_lado INT,
	dh_created_at TIMESTAMP,
	dh_updated_at TIMESTAMP
);

CREATE TABLE if not exists estrategias (
	id SERIAL PRIMARY KEY,
	estrategia VARCHAR(50) NOT NULL,
	descricao VARCHAR(50) ,
	tipo VARCHAR(1),
	dh_created_at TIMESTAMP,
	dh_updated_at TIMESTAMP,
	status VARCHAR(1) NOT NULL DEFAULT 'A'
);

CREATE TABLE if not exists estrategias_por_acao (
	id SERIAL PRIMARY KEY,
	acao_Id int NOT NULL ,
	estrategia_Id int NOT NULL ,
	dh_created_at TIMESTAMP,
	dh_updated_at TIMESTAMP,
	valorcompra float8,
	valorcompraFinal float8,
	tipo VARCHAR(1),
	status VARCHAR(1) NOT NULL DEFAULT 'A',
	CONSTRAINT fk_id_acaoId FOREIGN KEY (acao_Id) REFERENCES acoes(id),
	CONSTRAINT fk_id_estrategias FOREIGN KEY (estrategia_Id) REFERENCES estrategias(id)
);

CREATE TABLE if not exists ordens (
	id SERIAL PRIMARY KEY,
	corretora_Id INT,
	acao_Id INT,
	acao VARCHAR(20),
	status VARCHAR(1) NOT NULL DEFAULT 'P',
	lucroPreju float8,
	valorSuj float8,
	valor float8,
	tipo VARCHAR(1) NOT NULL DEFAULT 'C',
	data_venda TIMESTAMP,
	data_compra TIMESTAMP,
	contratos INT,
	valoracaoatual float8,
	shortname  VARCHAR(200),
	mudou_lado INT,
	dh_created_at TIMESTAMP,
	dh_updated_at TIMESTAMP,
	loss float8,
	gain float8,
	compra_Amercado INT,
	is_Percentual_Loss_Gain INT,
	loss_Corrente float8,
	gain_Corrente float8,
	CONSTRAINT fk_id_acaoId FOREIGN KEY (acao_Id) REFERENCES acoes(id),
	CONSTRAINT fk_id_estrategias FOREIGN KEY (corretora_id) REFERENCES corretoras(id)
);

CREATE TABLE if not exists erros (
	id SERIAL PRIMARY KEY,
	ordem_Id int NOT NULL ,
	dh_created_at TIMESTAMP,
	dh_updated_at TIMESTAMP,
	erro VARCHAR(255),
	tipo VARCHAR(1),
	qtd_tentativas int, 
	price float8,
	loss float8,
	gain float8,
	contratos int ,
	CONSTRAINT fk_id_erros FOREIGN KEY (ordem_Id) REFERENCES ordens(id)
);

CREATE TABLE if not exists breakevens (
	id SERIAL PRIMARY KEY,
	ordem_Id int NOT NULL ,
	loss_Atual float8 NOT NULL ,
	gain_Atual float8 NOT NULL ,
	dh_created_at TIMESTAMP,
	dh_updated_at TIMESTAMP,
	valor_Atual_Acao float8,
	status VARCHAR(1) NOT NULL DEFAULT 'A',
	acao varchar(12),
	erro varchar(244),
	CONSTRAINT fk_id_breakeven FOREIGN KEY (ordem_Id) REFERENCES ordens(id)
);




