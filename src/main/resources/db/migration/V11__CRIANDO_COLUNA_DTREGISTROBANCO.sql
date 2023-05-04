alter table boletos add column data_registro_banco timestamp;

update boletos set data_registro_banco = current_date  where status = 1 and status_banco = 1 and data_registro_banco is null;