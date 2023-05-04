ALTER TABLE acoes ADD COLUMN acao_origem INT;
ALTER TABLE estrategias_por_acao ADD COLUMN tipo VARCHAR(1);
ALTER TABLE estrategias_por_acao ADD COLUMN quantidade INT;