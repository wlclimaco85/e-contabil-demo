-- CONTAS ATIVAS
create view public.view_pag_contas_ativas (banco, descricao_banco, data_criacao, quantidade) 
as select contas_ativas.banco,contas_ativas.descricao_banco,contas_ativas.data_criacao, contas_ativas.quantidade
from (
select c.codbco as banco, 
	case c.codbco when 1 then '001 - Banco do Brasil'
	when 341 then '341 - Banco Itaú' end as descricao_banco,
	TO_DATE(to_char(current_date, 'dd/MM/yyyy'), 'dd/MM/yyyy') as data_criacao, count(*) as quantidade
from contas c where c.statusapi = 'S' group by banco
) contas_ativas
group by contas_ativas.banco, contas_ativas.descricao_banco, contas_ativas.data_criacao, contas_ativas.quantidade;

-- CONTAS ATIVADAS
create view public.view_pag_contas_ativadas (banco, descricao_banco, data_criacao, quantidade) 
as select contas_ativadas.banco, contas_ativadas.descricao_banco, contas_ativadas.data_criacao, SUM(contas_ativadas.quantidade) as quantidade 
from(
select 
    case c.codbco when 1 then '001 - Banco do Brasil'
	when 341 then '341 - Banco Itaú' end as descricao_banco,
    c.codbco as banco, TO_DATE(to_char(c.dh_created_at, 'dd/MM/yyyy'), 'dd/MM/yyyy') as data_criacao, count(*) as quantidade 
	from contas c where statusapi = 'S' group by data_criacao,banco
) contas_ativadas 
group by data_criacao,banco,descricao_banco;

-- CONTAS INATIVAS
create view public.view_pag_contas_inativas (banco, descricao_banco, data_desativacao, quantidade) 
as select contas_inativas.banco, contas_inativas.descricao_banco, contas_inativas.data_desativacao, SUM(contas_inativas.quantidade) as quantidade 
from(
select c.codbco as banco,
 	case c.codbco when 1 then '001 - Banco do Brasil'
	when 341 then '341 - Banco Itaú' end as descricao_banco,
	TO_DATE(to_char(c.dh_desativacao, 'dd/MM/yyyy'), 'dd/MM/yyyy') as data_desativacao, count(*) as quantidade
from contas c where statusapi = 'N' group by data_desativacao,banco  
) contas_inativas 
group by data_desativacao,banco,descricao_banco;

-- CONTA EMITE PIX
create view public.view_pag_contas_emite_pix (indicador_pix, descricao_banco, banco, data_emissao) 
as select contas_emite_pix.indicador_pix, contas_emite_pix.descricao_banco, contas_emite_pix.banco, contas_emite_pix.data_emissao 
from(
select
	case b.indicador_pix when 'S' then 'Sim'
	when 'N' then 'Não' end as indicador_pix,
    case c.codbco when 1 then '001 - Banco do Brasil'
	when 341 then '341 - Banco Itaú' end as descricao_banco,
    c.codbco as banco,
	TO_DATE(to_char(c.dh_created_at, 'dd/MM/yyyy'), 'dd/MM/yyyy') as data_emissao
from
	boletos b
join contas c on
	c.id = b.idapibanco
where b.id in (select max(id) as id  from boletos l group by l.idapibanco)
) contas_emite_pix;
