package br.com.boleto.persistence.repository;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import br.com.boleto.persistence.dtos.StatusBoletoFilterRequestDto;
import br.com.boleto.persistence.entity.Boleto;
import br.com.boleto.util.DateUtil;

@Repository
public class BoletoCustomRepository{
	
	private final EntityManager em;
	
	public BoletoCustomRepository(EntityManager em) {
		this.em = em;
	}
	
	public List<Boleto> findByRequest(StatusBoletoFilterRequestDto filter, Pageable pageable){
		String query = "select b from Boleto b";
		String condicao = " where ";

		query = adicionaJoin(query, filter);
		
		if (filter.getNumeroInscricaoPagador() != null && !filter.getNumeroInscricaoPagador().isBlank()) {
			query += condicao + "p.numeroInscricao = :numeroInscricaoPagador";
			condicao = " and ";
		}
		
		if (filter.getStatus() != null && !filter.getStatus().isEmpty()) {
			query += condicao + "b.status in (:status)";
			condicao = " and ";
		}
		
		if (filter.getIdApiBanco() != null && !filter.getIdApiBanco().isEmpty()) {
			query += condicao + "b.conta.id in (:contaId)";
			condicao = " and ";
		}
		
		if (filter.getNumeroTituloBeneficiario() != null && !filter.getNumeroTituloBeneficiario().isEmpty()) {
			query += condicao + "b.numeroTituloBeneficiario in (:numeroTituloBeneficiario)";
			condicao = " and ";
		}
		
		if (filter.getNumeroTituloCliente() != null && !filter.getNumeroTituloCliente().isBlank()) {
			query += condicao + "b.numeroTituloCliente like '%" + filter.getNumeroTituloCliente() + "%'";
			condicao = " and ";
		}
		
		if (filter.getDataVencimentoInicial() != null && !filter.getDataVencimentoInicial().isBlank()) {
			query += (filter.getDataVencimentoFinal() != null && !filter.getDataVencimentoFinal().isBlank()) ? 
					condicao + "b.dataVencimento between :dataVencimentoInicial and :dataVencimentoFinal" : 
						condicao + "b.dataVencimento >= :dataVencimentoInicial";
			condicao = " and ";
		}
		
		if (filter.getDataEmissaoInicial() != null && !filter.getDataEmissaoInicial().isBlank()) {
			query += (filter.getDataEmissaoFinal() != null && !filter.getDataEmissaoFinal().isBlank()) ?
					condicao + "b.dataEmissao between :dataEmissaoInicial and :dataEmissaoFinal" : 
						condicao + "b.dataEmissao >= :dataEmissaoInicial";
			condicao = " and ";
		}
		
		var q = em.createQuery(query, Boleto.class);
		
		
		if (filter.getNumeroInscricaoPagador() != null && !filter.getNumeroInscricaoPagador().isBlank()) {
			q.setParameter("numeroInscricaoPagador", filter.getNumeroInscricaoPagador());
		}
		
		if (filter.getStatus() != null && !filter.getStatus().isEmpty()) {
			q.setParameter("status", filter.getStatus());
		}
		
		if (filter.getIdApiBanco() != null && !filter.getIdApiBanco().isEmpty()) {
			q.setParameter("contaId", filter.getIdApiBanco());
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
	
	private String adicionaJoin(String query, StatusBoletoFilterRequestDto filter) {
		String leftJoinContas = " left join Conta c on (b.conta.id = c.id) ";
		String leftJoinPagador = " left join Pagador p on (b.pagador.id = p.id) ";
		
		if (filter.getIdApiBanco() != null && !filter.getIdApiBanco().isEmpty()) {	
			query += leftJoinContas;
		}
		
		if (filter.getNumeroInscricaoPagador() != null && !filter.getNumeroInscricaoPagador().isBlank()) {
			query += leftJoinPagador;
		}
		
		return query;
	}
}