UPDATE boletos SET beneficiario_final_id = null WHERE beneficiario_final_id = 0;
UPDATE boletos SET pagador_id = null WHERE pagador_id = 0;
UPDATE boletos SET juros_mora_id = null WHERE juros_mora_id = 0;
UPDATE boletos SET multa_id = null WHERE multa_id = 0;
UPDATE boletos SET desconto_id = null WHERE desconto_id = 0;
UPDATE boletos SET segundo_desconto_id = null WHERE segundo_desconto_id = 0;
UPDATE boletos SET terceiro_desconto_id = null WHERE terceiro_desconto_id = 0;

ALTER TABLE boletos
ADD CONSTRAINT fk_id_conta FOREIGN KEY (idapibanco) REFERENCES contas(id);

ALTER TABLE boletos
ADD CONSTRAINT fk_id_beneficiario_final FOREIGN KEY (beneficiario_final_id) REFERENCES beneficiariofinal(id);

ALTER TABLE boletos
ADD CONSTRAINT fk_id_pagador FOREIGN KEY (pagador_id) REFERENCES pagador(id);

ALTER TABLE boletos
ADD CONSTRAINT fk_id_juros_mora FOREIGN KEY (juros_mora_id) REFERENCES jurosmora(id);

ALTER TABLE boletos
ADD CONSTRAINT fk_id_multa FOREIGN KEY (multa_id) REFERENCES multa(id);

ALTER TABLE boletos
ADD CONSTRAINT fk_id_desconto FOREIGN KEY (desconto_id) REFERENCES desconto(id);

ALTER TABLE boletos
ADD CONSTRAINT fk_id_segundo_desconto FOREIGN KEY (segundo_desconto_id) REFERENCES desconto(id);

ALTER TABLE boletos
ADD CONSTRAINT fk_id_terceiro_desconto FOREIGN KEY (terceiro_desconto_id) REFERENCES desconto(id);
