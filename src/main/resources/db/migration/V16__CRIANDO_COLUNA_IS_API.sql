ALTER TABLE boletos ADD COLUMN is_api BOOLEAN DEFAULT false;

UPDATE boletos b SET is_api= true where is_api = false;

