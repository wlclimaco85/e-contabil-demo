--BOLETOS EMITIDOS X BOLETOS LIQUIDADOS
create or replace view public.view_boletos_emitidos_liquidados (data_emissao, status, quantidade) 
as select * from (
select boletos_liquidados.dtemit ,boletos_liquidados.status, count(*) as quantidade
from 
(
	select c.registrobase, p.id, p.nome, TO_DATE(to_char(b.data_emissao, 'MM/yyyy'), 'MM/yyyy') as dtemit, c.dh_created_at, 'Liquidados' as status
	from boletos b 
	inner join contas c on c.id = b.conta_id 
	inner join parceiros p on p.id = c.idparceiro
	where numero_titulo_beneficiario is not null
	and b.status in (6,11)
) as boletos_liquidados
group by boletos_liquidados.dtemit, boletos_liquidados.status
union 
select boletos_emitidos.dtemit , boletos_emitidos.status, count(*)
from 
(
	select c.registrobase, p.id, p.nome, TO_DATE(to_char(b.data_emissao, 'MM/yyyy'), 'MM/yyyy') as dtemit, c.dh_created_at, 'Emitidos' as status
	from boletos b 
	inner join contas c on c.id = b.conta_id 
	inner join parceiros p on p.id = c.idparceiro
	where numero_titulo_beneficiario is not null
) as boletos_emitidos
group by boletos_emitidos.dtemit, boletos_emitidos.status
) dash1 order by dash1.dtemit, dash1.status;

--BOLETOS EMITIDOS
create or replace view public.view_boletos_emitidos (data_emissao, id_ambiente, empresa, status, quantidade) 
as select boletos_emitidos.dtemit, boletos_emitidos.registrobase, boletos_emitidos.nome, boletos_emitidos.status, SUM(boletos_emitidos.quantidade) as quantidade from
(
select c.registrobase, p.id, p.nome, TO_DATE(to_char(b.data_emissao, 'MM/yyyy'), 'MM/yyyy') as dtemit, c.dh_created_at, 'Boletos Emitidos' as status, count(*) as quantidade
	from boletos b 
	inner join contas c on c.id = b.conta_id 
	inner join parceiros p on p.id = c.idparceiro
	where numero_titulo_beneficiario is not null	
	group by dtemit, status, c.registrobase, p.id, p.nome, c.dh_created_at
) boletos_emitidos
group by boletos_emitidos.dtemit, boletos_emitidos.nome, boletos_emitidos.registrobase, boletos_emitidos.status;

--BOLETOS LIQUIDADOS
create or replace view public.view_boletos_liquidados (data_emissao, id_ambiente, empresa, status, quantidade) 
as select boletos_liquidados.dtemit, boletos_liquidados.registrobase, boletos_liquidados.nome, boletos_liquidados.status, SUM(boletos_liquidados.quantidade) as quantidade from
(
select c.registrobase, p.id, p.nome, TO_DATE(to_char(b.data_emissao, 'MM/yyyy'), 'MM/yyyy') as dtemit, c.dh_created_at, 'Boletos Liquidados' as status, count(*) as quantidade
	from boletos b 
	inner join contas c on c.id = b.conta_id 
	inner join parceiros p on p.id = c.idparceiro
	where numero_titulo_beneficiario is not null
	and b.status in (6,11)
	group by dtemit, status, c.registrobase, p.id, p.nome, c.dh_created_at
) boletos_liquidados
group by boletos_liquidados.dtemit, boletos_liquidados.nome, boletos_liquidados.registrobase, boletos_liquidados.status;

-- CONTAS ATIVAS
create or replace view public.view_contas_ativas (mes_atual, quantidade) 
as select contas_ativas.created_at, count(*) as quantidade
from (
select c.registrobase, c.dh_created_at, TO_DATE(to_char(current_date, 'MM/yyyy'), 'MM/yyyy') as created_at, c.id, p.id, p.nome 
from contas c 
inner join parceiros p on p.id = c.idparceiro
where c.statusapi = 'S'
) contas_ativas
group by contas_ativas.created_at;

--CONTAS ATIVAS COMPLETO (FILTROS)
create or replace view public.view_contas_ativas_filtros (id_ambiente, data_criacao_conta, mes_atual, id_conta, id_empresa, empresa) 
as select c.registrobase, c.dh_created_at,  TO_DATE(to_char(current_date, 'MM/yyyy'), 'MM/yyyy') created_at, c.id as id_conta, p.id as id_parceiro, p.nome 
from contas c 
inner join parceiros p on p.id = c.idparceiro
where c.statusapi = 'S';

--INFORMACOES DAS CONTAS
create or replace view public.view_info_contas (id_empresa, empresa, conta, cod_conta, data_ativacao, id_status_api, status_api, id_ambiente, ambiente, boletos_emitidos, boletos_liquidados) 
as select p.id, p.nome, c.codctabco, c.codcta, c.dh_created_at, c.statusapi,
case c.statusapi when 'S' then 'Sim'
when 'N' then 'Não' end as status_api, c.registrobase,
case c.registrobase when 0 then 'Não Registrada'
when 1 then 'Teste'
when 2 then 'Treinamento'
when 3 then 'Produção'end as ambiente,
(select count(1) from boletos b where b.conta_id = c.id and b.numero_titulo_beneficiario is not null and (b.status > 0 and b.status <> 100 )) as boletos_emitidos, 
(select count(1) from boletos b where b.conta_id = c.id and b.numero_titulo_beneficiario is not null and b.status in (6,11)) as boletos_liquidados
from contas c 
inner join parceiros p on p.id = c.idparceiro;

-- RECEITA BOLETOS LIQUIDADOS - PAGINA FATURAMENTO - RECEITA
create or replace view public.view_faturamento_receita (id_banco, banco, data_liquidacao, status, quantidade, receita_por_boleto, receita_total) 
as select receita_boletos_liquidados.banco, receita_boletos_liquidados.descricao_banco, receita_boletos_liquidados.dt_liquidacao, receita_boletos_liquidados.status, SUM(receita_boletos_liquidados.quantidade) as quantidade, receita_boletos_liquidados.receita_por_boleto, SUM((receita_por_boleto * quantidade)) as receita_total 
from (
	select TO_DATE(to_char(b.dh_credito_liquidacao, 'dd/MM/yyyy'), 'dd/MM/yyyy') as dt_liquidacao, 'Receitas - Boletos Liquidados' as status, count(*) as quantidade, c.banco_id as banco,
		case c.banco_id when 1 then '001 - Banco do Brasil'
		when 341 then '341 - Banco Itaú' end as descricao_banco,
		case c.banco_id when 1 then 0.04
		when 341 then 0.10 end as receita_por_boleto
	from boletos b
	inner join contas c on c.id = b.conta_id 
	inner join parceiros p on p.id = c.idparceiro
	where numero_titulo_beneficiario is not null
	and b.status in (6,11)
	group by dt_liquidacao, status, banco
) receita_boletos_liquidados group by receita_boletos_liquidados.dt_liquidacao, receita_boletos_liquidados.status, receita_boletos_liquidados.banco, receita_boletos_liquidados.receita_por_boleto, receita_boletos_liquidados.descricao_banco;

-- CONTAS ATIVAS
create or replace view public.view_pag_contas_ativas (banco, descricao_banco, data_criacao, quantidade)
as select contas_ativas.banco,contas_ativas.descricao_banco,contas_ativas.data_criacao, contas_ativas.quantidade
from (
select c.banco_id as banco, 
	case c.banco_id when 1 then '001 - Banco do Brasil'
	when 341 then '341 - Banco Itaú' end as descricao_banco,
	TO_DATE(to_char(current_date, 'dd/MM/yyyy'), 'dd/MM/yyyy') as data_criacao, count(*) as quantidade
from contas c where c.statusapi = 'S' and c.registrobase = 3 group by banco
) contas_ativas
group by contas_ativas.banco, contas_ativas.descricao_banco, contas_ativas.data_criacao, contas_ativas.quantidade;

-- CONTAS ATIVADAS
create or replace view public.view_pag_contas_ativadas (banco, descricao_banco, data_criacao, quantidade)
as select contas_ativadas.banco, contas_ativadas.descricao_banco, contas_ativadas.data_criacao, SUM(contas_ativadas.quantidade) as quantidade 
from(
select 
    case c.banco_id when 1 then '001 - Banco do Brasil'
	when 341 then '341 - Banco Itaú' end as descricao_banco,
    c.banco_id as banco, TO_DATE(to_char(c.dh_created_at, 'dd/MM/yyyy'), 'dd/MM/yyyy') as data_criacao, count(*) as quantidade 
	from contas c where statusapi = 'S' and c.registrobase = 3 group by data_criacao,banco
) contas_ativadas 
group by data_criacao,banco,descricao_banco;

-- CONTAS INATIVAS
create or replace view public.view_pag_contas_inativas (banco, descricao_banco, data_desativacao, quantidade)
as select contas_inativas.banco, contas_inativas.descricao_banco, contas_inativas.data_desativacao, SUM(contas_inativas.quantidade) as quantidade 
from(
select c.banco_id as banco,
 	case c.banco_id when 1 then '001 - Banco do Brasil'
	when 341 then '341 - Banco Itaú' end as descricao_banco,
	TO_DATE(to_char(c.dh_desativacao, 'dd/MM/yyyy'), 'dd/MM/yyyy') as data_desativacao, count(*) as quantidade
from contas c where statusapi = 'N' and c.registrobase = 3 group by data_desativacao,banco
) contas_inativas 
group by data_desativacao,banco,descricao_banco;

-- CONTA EMITE PIX
create or replace view public.view_pag_contas_emite_pix (indicador_pix, descricao_banco, banco, data_emissao)
as select contas_emite_pix.indicador_pix, contas_emite_pix.descricao_banco, contas_emite_pix.banco, contas_emite_pix.data_emissao 
from(
select
	case b.indicador_pix when 'S' then 'Sim'
	when 'N' then 'Não' end as indicador_pix,
    case c.banco_id when 1 then '001 - Banco do Brasil'
	when 341 then '341 - Banco Itaú' end as descricao_banco,
    c.banco_id as banco,
	TO_DATE(to_char(c.dh_created_at, 'dd/MM/yyyy'), 'dd/MM/yyyy') as data_emissao
from
	boletos b
join contas c on
	c.id = b.conta_id
	and c.registrobase = 3

where b.id in (select max(id) as id  from boletos l group by l.conta_id)
) contas_emite_pix;

-- BOLETOS EMITIDOS X BOLETOS LIQUIDADOS (PAGINA BOLETOS)
create or replace view public.view_boletos_emitidos_x_liquidados (empresa, data_emissao, status, quantidade, id_ambiente, ambiente, id_banco, banco)
as select dash1.nome, dash1.dtemit, dash1.status, dash1.quantidade, dash1.registrobase,
   case dash1.registrobase when 0 then 'Não Registrada'
   when 1 then 'Teste'
   when 2 then 'Treinamento'
   when 3 then 'Produção'end as ambiente, dash1.banco,
   case dash1.banco when 1 then '001 - Banco do Brasil'
   when 341 then '341 - Banco Itaú' end as descricao_banco
   from (
   select boletos_liquidados.nome, boletos_liquidados.registrobase, boletos_liquidados.dtemit, boletos_liquidados.status, boletos_liquidados.banco, SUM(boletos_liquidados.quantidade) as quantidade from (
   select c.registrobase, p.id, p.nome, TO_DATE(to_char(b.dh_credito_liquidacao , 'dd/MM/yyyy'), 'dd/MM/yyyy') as dtemit, c.banco_id as banco, 'Boletos Liquidados' as status, count(*) as quantidade
   	from boletos b
   	inner join contas c on c.id = b.conta_id
   	inner join parceiros p on p.id = c.idparceiro
   	where numero_titulo_beneficiario is not null
   	and b.dh_credito_liquidacao is not null
   	and b.status in (6,11)
   	group by dtemit, status, c.registrobase, p.id, p.nome, banco
   ) boletos_liquidados group by boletos_liquidados.dtemit, boletos_liquidados.status, boletos_liquidados.registrobase, boletos_liquidados.nome, boletos_liquidados.banco
   union
   select boletos_emitidos.nome, boletos_emitidos.registrobase, boletos_emitidos.dtemit, boletos_emitidos.status, boletos_emitidos.banco, SUM(boletos_emitidos.quantidade) as quantidade from (
   select c.registrobase, p.id, p.nome, TO_DATE(to_char(b.data_registro_banco , 'dd/MM/yyyy'), 'dd/MM/yyyy') as dtemit, c.banco_id as banco, 'Boletos Emitidos' as status, count(*) as quantidade
   	from boletos b
   	inner join contas c on c.id = b.conta_id
   	inner join parceiros p on p.id = c.idparceiro
   	where numero_titulo_beneficiario is not null
   	and b.data_registro_banco is not null
   	group by dtemit, status, c.registrobase, p.id, p.nome, banco
   ) boletos_emitidos group by boletos_emitidos.dtemit, boletos_emitidos.status, boletos_emitidos.registrobase, boletos_emitidos.nome, boletos_emitidos.banco
   )dash1 group by dash1.dtemit, dash1.status, dash1.quantidade, dash1.registrobase, dash1.nome, dash1.banco order by dash1.dtemit, dash1.status;

-- CLIENTES (PAGINA CLIENTES)
create  or replace view public.view_clientes (id_empresa, empresa, id_banco, banco, cod_conta, conta, data_ativacao, id_status_api, status_api, id_ambiente, ambiente, boletos_emitidos, boletos_liquidados, ultimo_boleto_registrado, total_contas, id_emite_pix, emite_pix)
as select info_contas.*, case emite_pix when 'S' then 'Sim'
when 'N' then 'Não' end as descricao_emite_pix
from(
select p.id, p.nome, c.banco_id,
case c.banco_id when 1 then '001 - Banco do Brasil'
when 341 then '341 - Banco Itaú' end as descricao_banco, c.codcta, c.codctabco, c.dh_created_at, c.statusapi,
case c.statusapi when 'S' then 'Sim'
when 'N' then 'Não' end as status_api, c.registrobase,
case c.registrobase when 0 then 'Não Registrada'
when 1 then 'Teste'
when 2 then 'Treinamento'
when 3 then 'Produção'end as ambiente,
(select count(1) from boletos b where b.conta_id = c.id and b.numero_titulo_beneficiario is not null and b.data_registro_banco is not null and (b.status > 0 and b.status <> 100 )) as boletos_emitidos,
(select count(1) from boletos b where b.conta_id = c.id and b.numero_titulo_beneficiario is not null and b.dh_credito_liquidacao is not null and b.status in (6,11)) as boletos_liquidados,
(select max(data_registro_banco) from boletos b where b.conta_id = c.id and b.data_registro_banco is not null and b.numero_titulo_beneficiario is not null) as ult_boleto_registrado,
count(c.id) as total_contas,
(select max(b.indicador_pix) from boletos b where b.conta_id = c.id and b.numero_titulo_beneficiario is not null and b.data_registro_banco is not null group by b.id order by b.id desc limit 1) as emite_pix
from contas c
inner join parceiros p on p.id = c.idparceiro
group by p.id, p.nome, c.banco_id, c.codctabco, c.codcta, c.dh_created_at, c.statusapi, c.registrobase, c.id
) as info_contas;