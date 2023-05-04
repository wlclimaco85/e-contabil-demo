ALTER TABLE certificados ADD COLUMN idconta INT;

UPDATE certificados SET idconta = idapibanco;

ALTER TABLE certificados ADD CONSTRAINT fk_certificados_conta FOREIGN KEY (idconta) REFERENCES contas (id);

ALTER TABLE certificados DROP COLUMN idapibanco;