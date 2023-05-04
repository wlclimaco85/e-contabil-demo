create  or replace view public.view_clientes (id_empresa, empresa, id_banco, banco, cod_conta, conta, data_ativacao, id_status_api, status_api, id_ambiente, ambiente, boletos_emitidos, boletos_liquidados, ultimo_boleto_registrado, total_contas, id_emite_pix, emite_pix)
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
(select count(1) from boletos b where b.idapibanco = c.id and b.numero_titulo_beneficiario is not null and b.data_registro_banco is not null and (b.status > 0 and b.status <> 100 )) as boletos_emitidos,
(select count(1) from boletos b where b.idapibanco = c.id and b.numero_titulo_beneficiario is not null and b.dh_credito_liquidacao is not null and b.status in (6,11)) as boletos_liquidados,
(select max(data_registro_banco) from boletos b where b.idapibanco = c.id and b.data_registro_banco is not null and b.numero_titulo_beneficiario is not null) as ult_boleto_registrado,
count(c.id) as total_contas,
(select max(b.indicador_pix) from boletos b where b.idapibanco = c.id and b.numero_titulo_beneficiario is not null and b.data_registro_banco is not null group by b.id order by b.id desc limit 1) as emite_pix
from contas c
inner join parceiros p on p.id = c.idparceiro
group by p.id, p.nome, c.codbco, c.codctabco, c.codcta, c.dh_created_at, c.statusapi, c.registrobase, c.id
) as info_contas;