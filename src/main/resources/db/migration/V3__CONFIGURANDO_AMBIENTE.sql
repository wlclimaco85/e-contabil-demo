ALTER TABLE boletos ADD COLUMN data_registro VARCHAR (100);
ALTER TABLE boletos ADD COLUMN estado_titulo_cobranca VARCHAR (100);
ALTER TABLE boletos ADD COLUMN contrato INT;
ALTER TABLE boletos ADD COLUMN data_movimento VARCHAR (100);
ALTER TABLE boletos ADD COLUMN data_credito VARCHAR (100);
ALTER TABLE boletos ADD COLUMN valor_atual FLOAT;
ALTER TABLE boletos ADD COLUMN valor_pago FLOAT;

ALTER TABLE boletos ADD COLUMN url VARCHAR;
ALTER TABLE boletos ADD COLUMN tx_id VARCHAR;
ALTER TABLE boletos ADD COLUMN emv VARCHAR;

ALTER TABLE boletos ADD COLUMN data_emissao_BKP TIMESTAMP;
ALTER TABLE boletos ADD COLUMN data_vencimento_BKP TIMESTAMP;

update boletos 
set data_emissao_BKP =  TO_DATE(data_emissao , 'DD.MM.YYYYY'),
data_vencimento_BKP = TO_DATE(data_vencimento , 'DD.MM.YYYYY');

ALTER TABLE boletos DROP COLUMN data_emissao;
ALTER TABLE boletos DROP COLUMN data_vencimento;

ALTER TABLE boletos ADD COLUMN data_emissao TIMESTAMP;
ALTER TABLE boletos ADD COLUMN data_vencimento TIMESTAMP;

update boletos 
set data_emissao  =  data_emissao_BKP,
data_vencimento  = data_vencimento_BKP;

ALTER TABLE boletos DROP COLUMN data_emissao_BKP;
ALTER TABLE boletos DROP COLUMN data_vencimento_BKP;

ALTER TABLE logEnvios ADD COLUMN tipo_evento INT;
ALTER TABLE logEnvios ADD COLUMN situacao INT;

ALTER TABLE pagador ADD COLUMN idparceiro INT;