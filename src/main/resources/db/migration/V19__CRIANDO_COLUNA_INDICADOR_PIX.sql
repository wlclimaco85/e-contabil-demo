ALTER TABLE contas ADD COLUMN indicador_pix VARCHAR (100);

UPDATE contas c SET indicador_pix='N' where indicador_pix is null;

