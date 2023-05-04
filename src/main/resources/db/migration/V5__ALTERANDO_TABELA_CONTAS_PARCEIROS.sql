ALTER TABLE contas ADD COLUMN clientid VARCHAR NOT NULL DEFAULT '';
ALTER TABLE contas ADD COLUMN clientsecret VARCHAR NOT NULL DEFAULT '';

UPDATE contas c SET clientid = p.clientid, clientsecret = p.clientsecret FROM parceiros p WHERE c.idparceiro  = p.id;

ALTER TABLE parceiros  DROP COLUMN clientid;
ALTER TABLE parceiros DROP COLUMN clientsecret;