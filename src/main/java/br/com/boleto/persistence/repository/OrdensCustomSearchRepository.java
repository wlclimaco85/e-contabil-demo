package br.com.boleto.persistence.repository;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import br.com.boleto.persistence.dtos.StatusBoletoFilterSearchRequestDto;
import br.com.boleto.persistence.entity.Ordens;
import br.com.boleto.util.DateUtil;

@Repository
public class OrdensCustomSearchRepository{
	
	private final EntityManager em;
	
	public OrdensCustomSearchRepository(EntityManager em) {
		this.em = em;
	}
	
	public List<Ordens> findByRequest(StatusBoletoFilterSearchRequestDto filter, Pageable pageable){
		
		String query = " select b from Boleto b ";
		query += " where 1=1 ";
		
		String condicao = " and ";

		
		if (filter.getNumeroInscricaoPagador() != null && !filter.getNumeroInscricaoPagador().isBlank()) {
			query += condicao + "p.numeroInscricao = :numeroInscricaoPagador";
		}
		
		if (filter.getNomeCliente() != null && !filter.getNomeCliente().isBlank()) {
			query += condicao + "pp.nome like '%:NomeCliente%'";
		}
		
		if (filter.getStatus() != null && !filter.getStatus().isEmpty()) {
			query += condicao + "b.status in (:status)";
		}
		
		if (filter.getIdApiBanco() != null && !filter.getIdApiBanco().isEmpty()) {
			query += condicao + "b.conta.id in (" + converterListEmInteger(filter.getIdApiBanco()) + ")";
		}
		
		if (filter.getNumeroTituloBeneficiario() != null && !filter.getNumeroTituloBeneficiario().isEmpty()) {
			query += condicao + "b.numeroTituloBeneficiario in (:numeroTituloBeneficiario)";
		}
		
		if (filter.getNumeroTituloCliente() != null && !filter.getNumeroTituloCliente().isEmpty()) {
			query += condicao + "b.numeroTituloCliente in (" + converterListEmString(filter.getNumeroTituloCliente()) + ") ";
		}
		
		if (filter.getDataVencimentoInicial() != null && !filter.getDataVencimentoInicial().isBlank()) {
			query += (filter.getDataVencimentoFinal() != null && !filter.getDataVencimentoFinal().isBlank()) ?
					condicao + "b.dataVencimento between :dataVencimentoInicial and :dataVencimentoFinal" :
						condicao + "b.dataVencimento >= :dataVencimentoInicial";
		}
		
		if (filter.getDataEmissaoInicial() != null && !filter.getDataEmissaoInicial().isBlank()) {
			query += (filter.getDataEmissaoFinal() != null && !filter.getDataEmissaoFinal().isBlank()) ?
					condicao + "b.dataEmissao between :dataEmissaoInicial and :dataEmissaoFinal" :
						condicao + "b.dataEmissao >= :dataEmissaoInicial";
		}
		
		var q = em.createQuery(query, Ordens.class);
		
		if (filter.getNumeroInscricaoPagador() != null && !filter.getNumeroInscricaoPagador().isBlank()) {
			q.setParameter("numeroInscricaoPagador", filter.getNumeroInscricaoPagador());
		}
		
		if (filter.getNomeCliente() != null && !filter.getNomeCliente().isBlank()) {
			q.setParameter("nomeCliente", filter.getNomeCliente());
		}
		
		if (filter.getStatus() != null && !filter.getStatus().isEmpty()) {
			q.setParameter("status", filter.getStatus());
		}
		
		if (filter.getNumeroTituloBeneficiario() != null && !filter.getNumeroTituloBeneficiario().isEmpty()) {
			q.setParameter("numeroTituloBeneficiario", filter.getNumeroTituloBeneficiario());
		}
		
		if (filter.getDataVencimentoInicial() != null && !filter.getDataVencimentoInicial().isBlank()) {
			q.setParameter("dataVencimentoInicial", DateUtil.convertStringToTimestamp(filter.getDataVencimentoInicial()));
			if (filter.getDataVencimentoFinal() != null && !filter.getDataVencimentoFinal().isBlank()) {
				q.setParameter("dataVencimentoFinal", DateUtil.convertStringToTimestamp(filter.getDataVencimentoFinal()));	
			}
		}
		
		if (filter.getDataEmissaoInicial() != null && !filter.getDataEmissaoInicial().isBlank()) {
			q.setParameter("dataEmissaoInicial", DateUtil.convertStringToTimestamp(filter.getDataEmissaoInicial()));
			if (filter.getDataEmissaoFinal() != null && !filter.getDataEmissaoFinal().isBlank()) {
				q.setParameter("dataEmissaoFinal", DateUtil.convertStringToTimestamp(filter.getDataEmissaoFinal()));
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
}