UPDATE contas SET dh_created_at = current_date WHERE dh_created_at IS NULL;
UPDATE boletos SET dh_created_at = current_date WHERE dh_created_at IS NULL;