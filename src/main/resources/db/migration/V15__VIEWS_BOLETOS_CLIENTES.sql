--BOLETOS EMITIDOS X BOLETOS LIQUIDADOS - PAGINA BOLETOS
create view public.view_boletos_emitidos_x_liquidados (empresa, data_emissao, status, quantidade, id_ambiente, ambiente, id_banco, banco)
as select dash1.nome, dash1.dtemit, dash1.status, dash1.quantidade, dash1.registrobase,
case dash1.registrobase when 0 then 'Não Registrada'
when 1 then 'Teste'
when 2 then 'Treinamento'
when 3 then 'Produção'end as ambiente, dash1.banco,
case dash1.banco when 1 then '001 - Banco do Brasil'
when 341 then '341 - Banco Itaú' end as descricao_banco
from (
select boletos_liquidados.nome, boletos_liquidados.registrobase, boletos_liquidados.dtemit, boletos_liquidados.status, boletos_liquidados.banco, SUM(boletos_liquidados.quantidade) as quantidade from (
select c.registrobase, p.id, p.nome, TO_DATE(to_char(b.data_emissao, 'dd/MM/yyyy'), 'dd/MM/yyyy') as dtemit, c.codbco as banco, 'Boletos Liquidados' as status, count(*) as quantidade
	from boletos b 
	inner join contas c on c.id = b.idapibanco 
	inner join parceiros p on p.id = c.idparceiro
	where numero_titulo_beneficiario is not null
	and b.status in (6,11)
	group by dtemit, status, c.registrobase, p.id, p.nome, banco
) boletos_liquidados group by boletos_liquidados.dtemit, boletos_liquidados.status, boletos_liquidados.registrobase, boletos_liquidados.nome, boletos_liquidados.banco
union
select boletos_emitidos.nome, boletos_emitidos.registrobase, boletos_emitidos.dtemit, boletos_emitidos.status, boletos_emitidos.banco, SUM(boletos_emitidos.quantidade) as quantidade from (
select c.registrobase, p.id, p.nome, TO_DATE(to_char(b.data_emissao, 'dd/MM/yyyy'), 'dd/MM/yyyy') as dtemit, c.codbco as banco, 'Boletos Emitidos' as status, count(*) as quantidade
	from boletos b 
	inner join contas c on c.id = b.idapibanco 
	inner join parceiros p on p.id = c.idparceiro
	where numero_titulo_beneficiario is not null
	group by dtemit, status, c.registrobase, p.id, p.nome, banco
) boletos_emitidos group by boletos_emitidos.dtemit, boletos_emitidos.status, boletos_emitidos.registrobase, boletos_emitidos.nome, boletos_emitidos.banco
)dash1 group by dash1.dtemit, dash1.status, dash1.quantidade, dash1.registrobase, dash1.nome, dash1.banco order by dash1.dtemit, dash1.status;

--INFORMACOES CONTAS - PAGINA CLIENTES
create view public.view_clientes (id_empresa, empresa, id_banco, banco, cod_conta, conta, data_ativacao, id_status_api, status_api, id_ambiente, ambiente, boletos_emitidos, boletos_liquidados, ultimo_boleto_registrado, total_contas, id_emite_pix, emite_pix) 
as select info_contas.*, case emite_pix when 'S' then 'Sim'
when 'N' then 'Não' end as descricao_emite_pix
from(
select p.id, p.nome, c.codbco, 
case c.codbco when 1 then '001 - Banco do Brasil'
when 341 then '341 - Banco Itaú' end as descricao_banco, c.codcta, c.codctabco, c.dh_created_at, c.statusapi,
case c.statusapi when 'S' then 'Sim'
when 'N' then 'Não' end as status_api, c.registrobase,
case c.registrobase when 0 then 'Não Registrada'
when 1 then 'Teste'
when 2 then 'Treinamento'
when 3 then 'Produção'end as ambiente,
(select count(1) from boletos b where b.idapibanco = c.id and b.numero_titulo_beneficiario is not null and (b.status > 0 and b.status <> 100 )) as boletos_emitidos, 
(select count(1) from boletos b where b.idapibanco = c.id and b.numero_titulo_beneficiario is not null and b.status in (6,11)) as boletos_liquidados,
(select max(b.data_registro_banco) from boletos b where b.idapibanco = c.id and b.numero_titulo_beneficiario is not null group by b.id order by b.data_registro_banco desc limit 1) as ult_boleto_registrado,
count(c.id) as total_contas,
(select max(b.indicador_pix) from boletos b where b.idapibanco = c.id and b.numero_titulo_beneficiario is not null group by b.id order by b.id desc limit 1) as emite_pix
from contas c
inner join parceiros p on p.id = c.idparceiro
group by p.id, p.nome, c.codbco, c.codctabco, c.codcta, c.dh_created_at, c.statusapi, c.registrobase, c.id
) as info_contas;