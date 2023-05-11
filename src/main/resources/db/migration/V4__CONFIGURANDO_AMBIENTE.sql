ALTER TABLE boletos ADD COLUMN cod_linha_digitavel VARCHAR (50);
ALTER TABLE boletos ADD COLUMN dh_credito_liquidacao TIMESTAMP;
ALTER TABLE boletos ADD COLUMN dh_recebimento_titulo TIMESTAMP;
ALTER TABLE boletos ADD COLUMN dh_created_at TIMESTAMP;
ALTER TABLE boletos ADD COLUMN dh_updated_at TIMESTAMP;

