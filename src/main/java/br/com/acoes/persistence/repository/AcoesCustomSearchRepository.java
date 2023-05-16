package br.com.acoes.persistence.repository;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import br.com.acoes.persistence.dtos.AcaoFilterSearchRequestDto;
import br.com.acoes.persistence.dtos.Acoes5Dto;
import br.com.acoes.persistence.entity.Acoes;
import br.com.acoes.persistence.entity.Ordens;
import br.com.acoes.util.DateUtil;

@Repository
public class AcoesCustomSearchRepository{
	
	private final EntityManager em;
	
	public AcoesCustomSearchRepository(EntityManager em) {
		this.em = em;
	}
	
	public List<Acoes> findByRequest(AcaoFilterSearchRequestDto filter, Pageable pageable){
		
		String query = " SELECT AC ";
			//	+ "AC.id, AC.acao, AC.dh_created_at, AC.status, AC.lucropreju,"
			//	+ " AC.valorsuj, AC.tipo, AC.periodo, AC.ambiente, AC.nomeRobo, AC.dataVenda, AC.dataCompra, AC.contratos, AC.valoracaoatual,"
			//	+ " AC.dh_updated_at, AC.shortname, AC.level, AC.mudouLado, AC.valor, AC.loss, AC.gain, AC.acaoOrigem, AC.compraAmercado, AC.isPercentualLossGain, "
			//	+ " AC.lossCorrente, AC.gainCorrente";
			//	+ " , e.erro,  0 as qtdBreakeven" ;
				//+ "(select count(b) from Breakeven b where b.acaoId = AC.id and b.status = P) as qtdBreakeven ";
		query += " FROM Acoes AC ";
		query = adicionaJoin(query, filter);
		query += " where 1=1 ";
		
		String condicao = " and ";

		
		if (filter.getOperacao() != null && !filter.getOperacao().isBlank()) {
			query += condicao + "AC.tipo = :tipo";
		}
		
		if (filter.getStatus() != null && !filter.getStatus().isBlank()) {
			query += condicao + "AC.status = :status";
		}
		
		if (filter.getId() != null && filter.getId() > 0) {
			query += condicao + "AC.id = :id";
		}
		
		if (filter.getLevel() != null) {
			query += condicao + "AC.level = :level";
		}
		
		if (filter.getMudouLado() != null && filter.getMudouLado() > 0) {
			query += condicao + "AC.mudouLado > 0 ";
		}
		
		if (filter.getNomeAcao() != null && !filter.getNomeAcao().isBlank()) {
			query += condicao + "AC.shortname like '%:nomeAcao%'";
		}
		if (filter.getAcao() != null && !filter.getAcao().isBlank()) {
			query += condicao + "AC.acao like '%"+filter.getAcao()+"%'";
		}
		
		if (filter.getIsLucro() != null && filter.getIsLucro()) {
			query += condicao + "AC.lucropreju > 0";
		}
		
		if (filter.getDataIndicacaoInicio() != null && !filter.getDataIndicacaoInicio().isBlank()) {
			query += (filter.getDataIndicacaoFinal() != null && !filter.getDataIndicacaoFinal().isBlank()) ?
					condicao + "AC.dh_created_at between :dataVencimentoInicial and :dataVencimentoFinal" :
						condicao + "AC.dh_created_at >= :dataVencimentoInicial";
		}
		
		if (filter.getValorInicioInicio() > 0) {
			query += (filter.getValorInicioFinal() > 0) ?
					condicao + "AC.valorsuj >= :dataEmissaoInicial and AC.VALORSUJ <= :dataEmissaoFinal" :
						condicao + "AC.valorsuj >= :dataEmissaoInicial";
		}
		query += " order by AC.level, AC.id";
		var q = em.createQuery(query, Acoes.class);
		
		if (filter.getOperacao() != null && !filter.getOperacao().isBlank()) {
			q.setParameter("tipo", filter.getOperacao());
		}
		
		if (filter.getStatus() != null && !filter.getStatus().isBlank()) {
			q.setParameter("status", filter.getStatus());
		}
		
		if (filter.getId() != null && filter.getId() > 0) {
			q.setParameter("id", filter.getId());
		}
		
		if (filter.getLevel() != null && filter.getLevel() > 0) {
			q.setParameter("level", filter.getLevel());
		}
		
		if (filter.getNomeAcao() != null && !filter.getNomeAcao().isBlank()) {
			q.setParameter("nomeAcao", filter.getLevel());
		}
		
		if (filter.getAcao() != null && !filter.getAcao().isBlank()) {
			//q.setParameter("acaooooo", filter.getAcao());
		}
		
		
		if (filter.getDataIndicacaoInicio() != null && !filter.getDataIndicacaoInicio().isBlank()) {
			q.setParameter("dataVencimentoInicial", DateUtil.convertStringToTimestamp(filter.getDataIndicacaoInicio()));
			if (filter.getDataIndicacaoInicio() != null && !filter.getDataIndicacaoFinal().isBlank()) {
				q.setParameter("dataVencimentoFinal", DateUtil.convertStringToTimestamp(filter.getDataIndicacaoFinal()));	
			}
		}
		
		if (filter.getValorInicioInicio() > 0) {
			q.setParameter("dataEmissaoInicial", filter.getValorInicioInicio());
			if (filter.getValorInicioFinal() > 0) {
				q.setParameter("dataEmissaoFinal", filter.getValorInicioFinal());
			}
		}

		if(pageable != null){
			q.setFirstResult((int) pageable.getOffset());
			q.setMaxResults(pageable.getPageSize());
		}
		
		return q.getResultList();
	}
	
public List<Ordens> findByRequestOrdens(AcaoFilterSearchRequestDto filter, Pageable pageable){
	
	String query = " SELECT AC ";
		//	+ "AC.id, AC.acao, AC.dh_created_at, AC.status, AC.lucropreju,"
		//	+ " AC.valorsuj, AC.tipo, AC.periodo, AC.ambiente, AC.nomeRobo, AC.dataVenda, AC.dataCompra, AC.contratos, AC.valoracaoatual,"
		//	+ " AC.dh_updated_at, AC.shortname, AC.level, AC.mudouLado, AC.valor, AC.loss, AC.gain, AC.acaoOrigem, AC.compraAmercado, AC.isPercentualLossGain, "
		//	+ " AC.lossCorrente, AC.gainCorrente";
		//	+ " , e.erro,  0 as qtdBreakeven" ;
			//+ "(select count(b) from Breakeven b where b.acaoId = AC.id and b.status = P) as qtdBreakeven ";
	query += " FROM Ordens AC ";
	query += " left join Erros e on (e.ordem.id = AC.id) ";
	query += " left join Acoes a on (AC.acao.id = a.id) ";
	query = adicionaJoin(query, filter);
	query += " where 1=1 ";
	
	String condicao = " and ";

	if (filter.getCorretoraId() != null && filter.getCorretoraId() > 0) {
		query += condicao + "AC.corretoraId = :coretoraId";
	}
	
	if (filter.getOperacao() != null && !filter.getOperacao().isBlank()) {
		query += condicao + "AC.tipo = :tipo";
	}
	
	if (filter.getStatus() != null && !filter.getStatus().isBlank()) {
		query += condicao + "AC.status = :status";
	}
	//TODO
//	if (filter.getAmbiente() != null) {
//		query += condicao + "AC.ambiente = :ambiente";
//	}
	
	
	
	if (filter.getId() != null && filter.getId() > 0) {
		query += condicao + "AC.id = :id";
	}
	
	if (filter.getLevel() != null) {
		query += condicao + "AC.level = :level";
	}
	
	if (filter.getMudouLado() != null && filter.getMudouLado() > 0) {
		query += condicao + "AC.mudouLado > 0 ";
	}
	
	if (filter.getNomeAcao() != null && !filter.getNomeAcao().isBlank()) {
		query += condicao + "AC.shortname like '%:nomeAcao%'";
	}
	if (filter.getAcao() != null && !filter.getAcao().isBlank()) {
		query += condicao + "AC.acao like '%"+filter.getAcao()+"%'";
	}
	
	if (filter.getIsLucro() != null && filter.getIsLucro()) {
		query += condicao + "AC.lucropreju > 0";
	}
	
	if (filter.getDataIndicacaoInicio() != null && !filter.getDataIndicacaoInicio().isBlank()) {
		query += (filter.getDataIndicacaoFinal() != null && !filter.getDataIndicacaoFinal().isBlank()) ?
				condicao + "AC.dh_created_at between :dataVencimentoInicial and :dataVencimentoFinal" :
					condicao + "AC.dh_created_at >= :dataVencimentoInicial";
	}
	
	if (filter.getValorInicioInicio() > 0) {
		query += (filter.getValorInicioFinal() > 0) ?
				condicao + "AC.valorsuj >= :dataEmissaoInicial and AC.VALORSUJ <= :dataEmissaoFinal" :
					condicao + "AC.valorsuj >= :dataEmissaoInicial";
	}
	query += " order by a.level, a.mudouLado, AC.id";
	var q = em.createQuery(query, Ordens.class);
	
	if (filter.getOperacao() != null && !filter.getOperacao().isBlank()) {
		q.setParameter("tipo", filter.getOperacao());
	}
	
	if (filter.getStatus() != null && !filter.getStatus().isBlank()) {
		q.setParameter("status", filter.getStatus());
	}
	
	if (filter.getAmbiente() != null) {
		//q.setParameter("ambiente", filter.getAmbiente());
	}
	
	if (filter.getId() != null && filter.getId() > 0) {
		q.setParameter("id", filter.getId());
	}
	
	if (filter.getCorretoraId() != null && filter.getCorretoraId() > 0) {
		q.setParameter("corretoraId", filter.getCorretoraId());
	}
	
	if (filter.getLevel() != null && filter.getLevel() > 0) {
		q.setParameter("level", filter.getLevel());
	}
	
	if (filter.getNomeAcao() != null && !filter.getNomeAcao().isBlank()) {
		q.setParameter("nomeAcao", filter.getLevel());
	}
	
	if (filter.getAcao() != null && !filter.getAcao().isBlank()) {
		//q.setParameter("acaooooo", filter.getAcao());
	}
	
	
	if (filter.getDataIndicacaoInicio() != null && !filter.getDataIndicacaoInicio().isBlank()) {
		q.setParameter("dataVencimentoInicial", DateUtil.convertStringToTimestamp(filter.getDataIndicacaoInicio()));
		if (filter.getDataIndicacaoInicio() != null && !filter.getDataIndicacaoFinal().isBlank()) {
			q.setParameter("dataVencimentoFinal", DateUtil.convertStringToTimestamp(filter.getDataIndicacaoFinal()));	
		}
	}
	
	if (filter.getValorInicioInicio() > 0) {
		q.setParameter("dataEmissaoInicial", filter.getValorInicioInicio());
		if (filter.getValorInicioFinal() > 0) {
			q.setParameter("dataEmissaoFinal", filter.getValorInicioFinal());
		}
	}

	if(pageable != null){
		q.setFirstResult((int) pageable.getOffset());
		q.setMaxResults(pageable.getPageSize());
	}
	
	return q.getResultList();
}
	

	private String converterListEmString(List<String> filter) {
		StringBuilder sql = new StringBuilder();
		for (String nossNum : filter) {
			sql.append("'").append(nossNum).append("',");
		}
		sql.append("'0'");
		sql = new StringBuilder(sql.toString().replace("[", "").replace("]", ""));
		return sql.toString();
	}
	
	private String converterListEmInteger(List<Integer> filter) {
		StringBuilder sql = new StringBuilder();
		for (Integer nossNum : filter) {
			sql.append("").append(nossNum).append(",");
		}
		sql.append("0");
		sql = new StringBuilder(sql.toString().replace("[", "").replace("]", ""));
		return sql.toString();
	}
	
	private String adicionaJoin(String query, AcaoFilterSearchRequestDto filter) {
		String leftJoinEstrategia = " LEFT JOIN ESTRATEGIAS_POR_ACAO EA ON (AC.ID = EA.ACAOID ) ";
		String leftJoinEstrategiaAcao = " ESTRATEGIAS E ON (E.ID = EA.ESTRATEGIAID) ";
		
		if (filter.getNomeEstrategia() != null && !filter.getNomeEstrategia().isEmpty()) {	
			query += leftJoinEstrategia;
			query += leftJoinEstrategiaAcao;
			query += "WHERE E.ESTRATEGIA LIKE '%"+ filter.getNomeEstrategia() +"%'";
		}
		
		return query;
	}
	
public List<Acoes5Dto> findByRequestErros(AcaoFilterSearchRequestDto filter, Pageable pageable){
		
		String query = " SELECT AC.*, e.erro, (select count(*) from breakeven b where b.acao_Id = a.id and b.status = P) as qtdBreakeven ";
		query += "FROM ACOES a ";
		query += " left join erros e on (e.acao_Id = a.id) ";
		query = adicionaJoin(query, filter);
		query += " where 1=1 ";
		
		String condicao = " and ";

		
		if (filter.getOperacao() != null && !filter.getOperacao().isBlank()) {
			query += condicao + "AC.tipo = :tipo";
		}
		
		if (filter.getStatus() != null && !filter.getStatus().isBlank()) {
			query += condicao + "AC.status = :status";
		}
		
		if (filter.getAmbiente() != null) {
			query += condicao + "AC.ambiente = :ambiente";
		}
		
		
		
		if (filter.getId() != null && filter.getId() > 0) {
			query += condicao + "AC.id = :id";
		}
		
		if (filter.getLevel() != null) {
			query += condicao + "AC.level = :level";
		}
		
		if (filter.getMudouLado() != null && filter.getMudouLado() > 0) {
			query += condicao + "AC.mudouLado > 0 ";
		}
		
		if (filter.getNomeAcao() != null && !filter.getNomeAcao().isBlank()) {
			query += condicao + "AC.shortname like '%:nomeAcao%'";
		}
		if (filter.getAcao() != null && !filter.getAcao().isBlank()) {
			query += condicao + "AC.acao like '%"+filter.getAcao()+"%'";
		}
		
		if (filter.getIsLucro() != null && filter.getIsLucro()) {
			query += condicao + "AC.lucropreju > 0";
		}
		
		if (filter.getDataIndicacaoInicio() != null && !filter.getDataIndicacaoInicio().isBlank()) {
			query += (filter.getDataIndicacaoFinal() != null && !filter.getDataIndicacaoFinal().isBlank()) ?
					condicao + "AC.dh_created_at between :dataVencimentoInicial and :dataVencimentoFinal" :
						condicao + "AC.dh_created_at >= :dataVencimentoInicial";
		}
		
		if (filter.getValorInicioInicio() > 0) {
			query += (filter.getValorInicioFinal() > 0) ?
					condicao + "AC.valorsuj >= :dataEmissaoInicial and AC.VALORSUJ <= :dataEmissaoFinal" :
						condicao + "AC.valorsuj >= :dataEmissaoInicial";
		}
		query += " order by AC.level, AC.mudouLado, AC.id";
		var q = em.createQuery(query, Acoes5Dto.class);
		
		if (filter.getOperacao() != null && !filter.getOperacao().isBlank()) {
			q.setParameter("tipo", filter.getOperacao());
		}
		
		if (filter.getStatus() != null && !filter.getStatus().isBlank()) {
			q.setParameter("status", filter.getStatus());
		}
		
		if (filter.getAmbiente() != null) {
			q.setParameter("ambiente", filter.getAmbiente());
		}
		
		if (filter.getId() != null && filter.getId() > 0) {
			q.setParameter("id", filter.getId());
		}
		
		if (filter.getLevel() != null && filter.getLevel() > 0) {
			q.setParameter("level", filter.getLevel());
		}
		
		if (filter.getNomeAcao() != null && !filter.getNomeAcao().isBlank()) {
			q.setParameter("nomeAcao", filter.getLevel());
		}
		
//		if (filter.getAcao() != null && !filter.getAcao().isBlank()) {
//			q.setParameter("acaooooo", filter.getAcao());
//		}
		
		
		if (filter.getDataIndicacaoInicio() != null && !filter.getDataIndicacaoInicio().isBlank()) {
			q.setParameter("dataVencimentoInicial", DateUtil.convertStringToTimestamp(filter.getDataIndicacaoInicio()));
			if (filter.getDataIndicacaoInicio() != null && !filter.getDataIndicacaoFinal().isBlank()) {
				q.setParameter("dataVencimentoFinal", DateUtil.convertStringToTimestamp(filter.getDataIndicacaoFinal()));	
			}
		}
		
		if (filter.getValorInicioInicio() > 0) {
			q.setParameter("dataEmissaoInicial", filter.getValorInicioInicio());
			if (filter.getValorInicioFinal() > 0) {
				q.setParameter("dataEmissaoFinal", filter.getValorInicioFinal());
			}
		}

		if(pageable != null){
			q.setFirstResult((int) pageable.getOffset());
			q.setMaxResults(pageable.getPageSize());
		}
		
		return q.getResultList();
	}
}