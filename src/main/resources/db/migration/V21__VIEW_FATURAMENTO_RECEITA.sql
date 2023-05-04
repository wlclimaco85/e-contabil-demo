-- RECEITA BOLETOS LIQUIDADOS - PAGINA FATURAMENTO - RECEITA
create view public.view_faturamento_receita (id_banco, banco, data_liquidacao, status, quantidade, receita_por_boleto, receita_total) 
as select receita_boletos_liquidados.banco, receita_boletos_liquidados.descricao_banco, receita_boletos_liquidados.dt_liquidacao, receita_boletos_liquidados.status, SUM(receita_boletos_liquidados.quantidade) as quantidade, receita_boletos_liquidados.receita_por_boleto, SUM((receita_por_boleto * quantidade)) as receita_total 
from (
	select TO_DATE(to_char(b.dh_credito_liquidacao, 'dd/MM/yyyy'), 'dd/MM/yyyy') as dt_liquidacao, 'Receitas - Boletos Liquidados' as status, count(*) as quantidade, c.codbco as banco,
		case c.codbco when 1 then '001 - Banco do Brasil'
		when 341 then '341 - Banco Itaú' end as descricao_banco,
		case c.codbco when 1 then 0.04
		when 341 then 0.10 end as receita_por_boleto
	from boletos b
	inner join contas c on c.id = b.idapibanco 
	inner join parceiros p on p.id = c.idparceiro
	where numero_titulo_beneficiario is not null
	and b.status in (6,11)
	group by dt_liquidacao, status, banco
) receita_boletos_liquidados group by receita_boletos_liquidados.dt_liquidacao, receita_boletos_liquidados.status, receita_boletos_liquidados.banco, receita_boletos_liquidados.receita_por_boleto, receita_boletos_liquidados.descricao_banco;