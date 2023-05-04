ALTER TABLE public.termos DROP CONSTRAINT termos_codserv_fkey;
ALTER TABLE public.contratos DROP CONSTRAINT contratos_idparceiro_fkey;
ALTER TABLE termos DROP COLUMN codserv;

ALTER TABLE contas DROP CONSTRAINT contas_bancoid_fkey;
ALTER TABLE contas DROP COLUMN  bancoid;
ALTER TABLE contas RENAME COLUMN codbco TO banco_id;
ALTER TABLE contas
ADD CONSTRAINT contas_banco_id_fkey FOREIGN KEY (banco_id) REFERENCES bancos(id);

ALTER TABLE boletos RENAME COLUMN idapibanco TO conta_id;
ALTER TABLE contratos RENAME COLUMN idapibanco TO conta_id;
ALTER TABLE certificados RENAME COLUMN idconta TO conta_id;

ALTER TABLE boletos DROP CONSTRAINT fk_id_conta;
ALTER TABLE contratos DROP CONSTRAINT contratos_idapibanco_fkey;
ALTER TABLE certificados DROP CONSTRAINT fk_certificados_conta;

ALTER TABLE boletos
ADD CONSTRAINT boletos_conta_id_fkey FOREIGN KEY (conta_id) REFERENCES contas(id);

ALTER TABLE contratos 
ADD CONSTRAINT contratos_conta_id_fkey FOREIGN KEY (conta_id) REFERENCES contas (id);

ALTER TABLE certificados 
ADD CONSTRAINT certificados_conta_id_fkey FOREIGN KEY (conta_id) REFERENCES contas (id);

ALTER TABLE boletos
RENAME CONSTRAINT fk_id_beneficiario_final TO boletos_beneficiario_final_id_fkey;

ALTER TABLE boletos
RENAME CONSTRAINT fk_id_pagador TO boletos_pagador_id_fkey;

ALTER TABLE boletos
RENAME CONSTRAINT fk_id_juros_mora TO boletos_juros_mora_id_fkey;

ALTER TABLE boletos
RENAME CONSTRAINT fk_id_multa TO boletos_multa_id_fkey;

ALTER TABLE boletos
RENAME CONSTRAINT fk_id_desconto TO boletos_desconto_id_fkey;

ALTER TABLE boletos
RENAME CONSTRAINT fk_id_segundo_desconto TO boletos_segundo_desconto_id_fkey;

ALTER TABLE boletos
RENAME CONSTRAINT fk_id_terceiro_desconto TO boletos_terceiro_desconto_id_fkey;


