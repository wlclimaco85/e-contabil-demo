--BOLETOS EMITIDOS X BOLETOS LIQUIDADOS
create view public.view_boletos_emitidos_liquidados (data_emissao, status, quantidade) 
as select * from (
select boletos_liquidados.dtemit ,boletos_liquidados.status, count(*) as quantidade
from 
(
	select c.registrobase, p.id, p.nome, TO_DATE(to_char(b.data_emissao, 'MM/yyyy'), 'MM/yyyy') as dtemit, c.dh_created_at, 'Liquidados' as status
	from boletos b 
	inner join contas c on c.id = b.idapibanco 
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
	inner join contas c on c.id = b.idapibanco 
	inner join parceiros p on p.id = c.idparceiro
	where numero_titulo_beneficiario is not null
) as boletos_emitidos
group by boletos_emitidos.dtemit, boletos_emitidos.status
) dash1 order by dash1.dtemit, dash1.status;

--BOLETOS EMITIDOS
create view public.view_boletos_emitidos (data_emissao, id_ambiente, empresa, status, quantidade) 
as select boletos_emitidos.dtemit, boletos_emitidos.registrobase, boletos_emitidos.nome, boletos_emitidos.status, SUM(boletos_emitidos.quantidade) as quantidade from
(
select c.registrobase, p.id, p.nome, TO_DATE(to_char(b.data_emissao, 'MM/yyyy'), 'MM/yyyy') as dtemit, c.dh_created_at, 'Boletos Emitidos' as status, count(*) as quantidade
	from boletos b 
	inner join contas c on c.id = b.idapibanco 
	inner join parceiros p on p.id = c.idparceiro
	where numero_titulo_beneficiario is not null	
	group by dtemit, status, c.registrobase, p.id, p.nome, c.dh_created_at
) boletos_emitidos
group by boletos_emitidos.dtemit, boletos_emitidos.nome, boletos_emitidos.registrobase, boletos_emitidos.status;

--BOLETOS LIQUIDADOS
create view public.view_boletos_liquidados (data_emissao, id_ambiente, empresa, status, quantidade) 
as select boletos_liquidados.dtemit, boletos_liquidados.registrobase, boletos_liquidados.nome, boletos_liquidados.status, SUM(boletos_liquidados.quantidade) as quantidade from
(
select c.registrobase, p.id, p.nome, TO_DATE(to_char(b.data_emissao, 'MM/yyyy'), 'MM/yyyy') as dtemit, c.dh_created_at, 'Boletos Liquidados' as status, count(*) as quantidade
	from boletos b 
	inner join contas c on c.id = b.idapibanco 
	inner join parceiros p on p.id = c.idparceiro
	where numero_titulo_beneficiario is not null
	and b.status in (6,11)
	group by dtemit, status, c.registrobase, p.id, p.nome, c.dh_created_at
) boletos_liquidados
group by boletos_liquidados.dtemit, boletos_liquidados.nome, boletos_liquidados.registrobase, boletos_liquidados.status;

-- CONTAS ATIVAS
create view public.view_contas_ativas (mes_atual, quantidade) 
as select contas_ativas.created_at, count(*) as quantidade
from (
select c.registrobase, c.dh_created_at, TO_DATE(to_char(current_date, 'MM/yyyy'), 'MM/yyyy') as created_at, c.id, p.id, p.nome 
from contas c 
inner join parceiros p on p.id = c.idparceiro
where c.statusapi = 'S'
) contas_ativas
group by contas_ativas.created_at;

--CONTAS ATIVAS COMPLETO (FILTROS)
create view public.view_contas_ativas_filtros (id_ambiente, data_criacao_conta, mes_atual, id_conta, id_empresa, empresa) 
as select c.registrobase, c.dh_created_at,  TO_DATE(to_char(current_date, 'MM/yyyy'), 'MM/yyyy') created_at, c.id as id_conta, p.id as id_parceiro, p.nome 
from contas c 
inner join parceiros p on p.id = c.idparceiro
where c.statusapi = 'S';

--INFORMACOES DAS CONTAS
create view public.view_info_contas (id_empresa, empresa, conta, cod_conta, data_ativacao, id_status_api, status_api, id_ambiente, ambiente, boletos_emitidos, boletos_liquidados) 
as select p.id, p.nome, c.codctabco, c.codcta, c.dh_created_at, c.statusapi,
case c.statusapi when 'S' then 'Sim'
when 'N' then 'Não' end as status_api, c.registrobase,
case c.registrobase when 0 then 'Não Registrada'
when 1 then 'Teste'
when 2 then 'Treinamento'
when 3 then 'Produção'end as ambiente,
(select count(1) from boletos b where b.idapibanco = c.id and b.numero_titulo_beneficiario is not null and (b.status > 0 and b.status <> 100 )) as boletos_emitidos, 
(select count(1) from boletos b where b.idapibanco = c.id and b.numero_titulo_beneficiario is not null and b.status in (6,11)) as boletos_liquidados
from contas c 
inner join parceiros p on p.id = c.idparceiro;