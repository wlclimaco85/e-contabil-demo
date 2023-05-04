ALTER TABLE contas ADD COLUMN descricao VARCHAR(40);
ALTER TABLE contas ADD COLUMN codigo_empresa INT;
ALTER TABLE contas ADD COLUMN emite_boleto VARCHAR(1) NOT NULL DEFAULT 'S';
ALTER TABLE contas ADD COLUMN ativa VARCHAR(1) NOT NULL DEFAULT 'S';
ALTER TABLE contas ADD COLUMN tipo_api_boleto VARCHAR(1);
ALTER TABLE contas ADD COLUMN data_registro_conta TIMESTAMP;
ALTER TABLE contas ADD COLUMN api_baixa_automatica VARCHAR(1);
ALTER TABLE contas ADD COLUMN api_conciliacao_automatica VARCHAR(1);
ALTER TABLE contas ADD COLUMN aceita_titulo_vencido VARCHAR(32);
ALTER TABLE contas ADD COLUMN recebimento_parcial VARCHAR(32);
ALTER TABLE contas ADD COLUMN recebimento_dias INT;
ALTER TABLE contas ADD COLUMN orgao_negativador INT;
ALTER TABLE contas ADD COLUMN quantidade_dias_protesto INT;
ALTER TABLE contas ADD COLUMN quantidade_dias_negativacao INT;
ALTER TABLE contas ADD COLUMN instrucao_protesto INT;
ALTER TABLE contas ADD COLUMN instrucao_negativacao INT;
ALTER TABLE contas ADD COLUMN contabilizar_dias BOOLEAN NOT NULL DEFAULT TRUE;
ALTER TABLE contas ADD COLUMN tipo_juros_mora VARCHAR(1);
ALTER TABLE contas ADD COLUMN valor_juros_mora FLOAT;
ALTER TABLE contas ADD COLUMN tipo_multa VARCHAR(1);
ALTER TABLE contas ADD COLUMN tipo_data_multa VARCHAR(1);
ALTER TABLE contas ADD COLUMN quantidade_dias_multa INT;
ALTER TABLE contas ADD COLUMN valor_multa FLOAT;

ALTER TABLE bancos ALTER COLUMN urlprod DROP NOT NULL;
ALTER TABLE bancos ALTER COLUMN urlhomol DROP NOT NULL;
ALTER TABLE bancos ALTER COLUMN urloauthprod DROP NOT NULL;
ALTER TABLE bancos ALTER COLUMN urloauthhomol DROP NOT NULL;

ALTER TABLE contas ALTER COLUMN clientid DROP NOT NULL;
ALTER TABLE contas ALTER COLUMN clientsecret DROP NOT NULL;