package br.com.boleto.persistence.repository;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import br.com.boleto.persistence.dtos.AcaoFilterSearchRequestDto;
import br.com.boleto.persistence.entity.Acoes;
import br.com.boleto.util.DateUtil;

@Repository
public class AcoesCustomSearchRepository{
	
	private final EntityManager em;
	
	public AcoesCustomSearchRepository(EntityManager em) {
		this.em = em;
	}
	
	public List<Acoes> findByRequest(AcaoFilterSearchRequestDto filter, Pageable pageable){
		
		String query = " SELECT AC FROM Acoes AC  ";
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
		var q = em.createQuery(query, Acoes.class);
		
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
}