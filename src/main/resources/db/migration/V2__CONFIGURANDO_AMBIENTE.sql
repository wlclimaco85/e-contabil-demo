create table if not exists  desconto  (
   id SERIAL PRIMARY KEY,
   tipo INT,
   data_Expiracao VARCHAR (100),
   porcentagem FLOAT,
   valor FLOAT
);

create table if not exists  jurosMora  (
   id SERIAL PRIMARY KEY,
   tipo INT,
   porcentagem FLOAT,
   valor FLOAT
);

create table if not exists  multa  (
   id SERIAL PRIMARY KEY,
   tipo INT,
   data VARCHAR (100),
   porcentagem FLOAT,
   valor FLOAT
);

create table if not exists  pagador  (
   id SERIAL PRIMARY KEY,
   tipo_Inscricao INT,
   numero_Inscricao VARCHAR (100),
   nome VARCHAR (100),
   endereco VARCHAR (254),
   cep INT,
   cidade VARCHAR (100),
   bairro VARCHAR (100),
   uf VARCHAR (2),
   telefone VARCHAR (20)
);

create table if not exists  boletos  (
   id SERIAL PRIMARY KEY,
   idapibanco  INT,
   nossonumero  INT,
   numero_convenio INT,
   numero_carteira INT,
   numero_variacao_carteira INT,
   codigo_modalidade INT,
   data_emissao VARCHAR (100),
   data_vencimento VARCHAR (100),
   valor_original FLOAT,
   valor_abatimento FLOAT,
   quantidade_dias_protesto INT,
   quantidade_dias_negativacao INT,
   orgao_negativador INT,
   indicador_aceite_titulo_vencido VARCHAR (100), 
   numero_dias_limite_recebimento INT,
   codigo_aceite VARCHAR (100), 
   codigo_tipo_titulo INT,
   descricao_tipo_titulo VARCHAR (100), 
   indicador_Permissao_Recebimento_Parcial VARCHAR (100), 
   numero_titulo_beneficiario INT, 
   campo_utilizacao_beneficiario VARCHAR (100), 
   numero_titulo_cliente VARCHAR (100), 
   mensagem_bloqueto_ocorrencia VARCHAR (100), 
   indicador_pix VARCHAR (100), 
   status INT, 
   desconto_id INT,
   segundo_desconto_id INT,
   terceiro_desconto_id INT,
   juros_mora_id INT,
   multa_id INT,
   pagador_Id INT,
   beneficiario_final_id INT,
   codigo_estado_titulo_cobranca INT,
   indicador_nova_data_vencimento VARCHAR (2) DEFAULT 'N',
   indicador_atribuir_desconto VARCHAR (2) DEFAULT 'N',
   indicador_alterar_desconto VARCHAR (2) DEFAULT 'N',
   indicador_alterar_data_desconto VARCHAR (2) DEFAULT 'N',
   indicador_protestar VARCHAR (2) DEFAULT 'N',
   indicador_sustacao_protesto VARCHAR (2) DEFAULT 'N',
   indicador_cancelar_protesto VARCHAR (2) DEFAULT 'N',
   indicador_incluir_abatimento VARCHAR (2) DEFAULT 'N',
   indicador_cancelar_abatimento VARCHAR (2) DEFAULT 'N',
   indicador_cobrar_juros VARCHAR (2) DEFAULT 'N',
   indicador_dispensar_juros VARCHAR (2) DEFAULT 'N',
   indicador_cobrar_multa VARCHAR (2) DEFAULT 'N',
   indicador_dispensar_multa VARCHAR (2) DEFAULT 'N',
   indicador_negativar VARCHAR (2) DEFAULT 'N',
   indicador_alterar_nosso_numero VARCHAR (2) DEFAULT 'N',
   indicador_alterar_endereco_pagador VARCHAR (2) DEFAULT 'N',
   indicador_alterar_prazo_boleto_vencido VARCHAR (2) DEFAULT 'N',
   tipo_negativacao INT,
   quantidade_dias_aceite INT
);

create table if not exists  logEnvios  (
   id SERIAL PRIMARY KEY,
   nossonumero  VARCHAR,
   dhocorrencia TIMESTAMP NOT NULL,
   mensagem VARCHAR,
   stacktrace VARCHAR,
   status INT NOT NULL
);
