ALTER TABLE boletos ADD COLUMN instrucao_protesto INT;
ALTER TABLE boletos ADD COLUMN instrucao_negativacao INT;
ALTER TABLE boletos ADD COLUMN contabilizar_dias BOOLEAN NOT NULL DEFAULT TRUE;