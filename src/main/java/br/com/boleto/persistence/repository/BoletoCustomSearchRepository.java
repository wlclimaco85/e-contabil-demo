package br.com.boleto.persistence.repository;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import br.com.boleto.persistence.dtos.ContaSearchRequestDto;
import br.com.boleto.persistence.dtos.LogEnvioSearchRequestDto;
import br.com.boleto.persistence.dtos.ParceiroSearchRequestDto;
import br.com.boleto.persistence.dtos.QtdStatusDto;
import br.com.boleto.persistence.dtos.StatusBoletoFilterSearchRequestDto;
import br.com.boleto.persistence.entity.Boleto;
import br.com.boleto.persistence.entity.Conta;
import br.com.boleto.persistence.entity.Job;
import br.com.boleto.persistence.entity.LogEnvio;
import br.com.boleto.persistence.entity.Parceiro;
import br.com.boleto.util.DateUtil;

@Repository
public class BoletoCustomSearchRepository{
	
	private final EntityManager em;
	
	public BoletoCustomSearchRepository(EntityManager em) {
		this.em = em;
	}
	
	public List<Boleto> findByRequest(StatusBoletoFilterSearchRequestDto filter, Pageable pageable){
		
		String query = " select b from Boleto b ";
		query = adicionaJoin(query, filter);
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
		
		var q = em.createQuery(query, Boleto.class);
		
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
	
	public List<Parceiro> findByParceiroRequest(ParceiroSearchRequestDto filter){

		String query = "select p from Parceiro p where 1=1";
	
		String condicao = " and ";
		if (filter.getNome() != null  && !filter.getNome().isBlank()) {
			query += condicao + "p.nome like :nome ";
		}
		
		if (filter.getId() != null  && filter.getId() > 0) {
			query += condicao + "p.id = :id ";
		}
		
		
		var q = em.createQuery(query, Parceiro.class);
		
		if (filter.getNome() != null && !filter.getNome().isEmpty()) {
			q.setParameter("nome", "%" + filter.getNome() +"%");
		}
		
		if (filter.getId() != null && filter.getId() > 0) {
			q.setParameter("id", filter.getId());
		}

		return q.getResultList();
	}
	
	public List<LogEnvio> findByLogEnvioRequest(LogEnvioSearchRequestDto filter){
		
		String query = "select l from LogEnvio l ";
		
		if((filter.getNumeroTituloBeneficiario() != null) || (filter.getIdApiBanco() != null)) {
			query += " left join Boleto b on (b.numeroTituloCliente = l.nossonumero)";
		}
		query += " where 1=1 \r\n";
	
		String condicao = " and ";
		if (filter.getNumeroTituloBeneficiario() != null  && filter.getNumeroTituloBeneficiario() > 0) {
			query += condicao + "b.numeroTituloBeneficiario = :numeroTituloBeneficiario ";
		}
		
		if (filter.getIdApiBanco() != null  && filter.getIdApiBanco() > 0) {
			query += condicao + "b.conta.id  = :contaId ";
		}
		if (filter.getNumeroTituloCliente() != null && !filter.getNumeroTituloCliente().isBlank()) {
			query += condicao + "l.nossonumero like :numeroTituloCliente ";
		}
		
		if (filter.getStatus() != null && !filter.getStatus().isEmpty()) {
			query += condicao + "l.status in (:status) ";
		}
		
		if (filter.getTipoEvento() != null && !filter.getTipoEvento().isEmpty()) {
			query += condicao + "l.tipoEvento in (:tipoEvento) ";
		}
		
		if (filter.getSituacao() != null && !filter.getSituacao().isEmpty()) {
			query += condicao + "l.situacao in (:situacao) ";
		}
		
		if (filter.getDataOcorrenciaInicial() != null && !filter.getDataOcorrenciaInicial().isBlank()) {
			if (filter.getDataOcorrenciaFinal() != null && !filter.getDataOcorrenciaFinal().isBlank()) {
				query += condicao + "l.dhocorrencia between TO_DATE(:dhocorrenciaInicial , 'DD.MM.YYYY') and TO_DATE(:dhocorrenciaFinal , 'DD.MM.YYYY')";
			} else {
				query += condicao + "l.dhocorrencia >= TO_DATE(:dhocorrenciaInicial , 'DD.MM.YYYY')";
			}
		}
		
		query += "order by l.dhocorrencia";
		
		var q = em.createQuery(query, LogEnvio.class);
		
		if (filter.getNumeroTituloBeneficiario() != null  && filter.getNumeroTituloBeneficiario() > 0) {
			q.setParameter("numeroTituloBeneficiario", filter.getNumeroTituloBeneficiario());
		}
		
		if (filter.getIdApiBanco() != null  && filter.getIdApiBanco() > 0) {
			q.setParameter("contaId", filter.getIdApiBanco());
		}
		if (filter.getNumeroTituloCliente() != null && !filter.getNumeroTituloCliente().isBlank()) {
			q.setParameter("numeroTituloCliente", "%"+filter.getNumeroTituloCliente() + "%");
		}
		
		if (filter.getStatus() != null && !filter.getStatus().isEmpty()) {
			q.setParameter("status", filter.getStatus());
		}
		
		if (filter.getTipoEvento() != null && !filter.getTipoEvento().isEmpty()) {
			q.setParameter("tipoEvento", filter.getTipoEvento());
		}
		
		if (filter.getSituacao() != null && !filter.getSituacao().isEmpty()) {
			query += condicao + "l.situacao in (:situacao) ";
			q.setParameter("situacao", filter.getSituacao());
		}
		
		if (filter.getDataOcorrenciaInicial() != null && !filter.getDataOcorrenciaInicial().isBlank()) {
			q.setParameter("dhocorrenciaInicial", DateUtil.convertStringToTimestamp(filter.getDataOcorrenciaInicial()));
			if (filter.getDataOcorrenciaFinal() != null && !filter.getDataOcorrenciaFinal().isBlank()) {
				q.setParameter("dhocorrenciaoFinal", DateUtil.convertStringToTimestamp(filter.getDataOcorrenciaFinal()));
			}
		}
		
		return q.getResultList();
		
	}
	
	public List<Job> findByJobRequest(StatusBoletoFilterSearchRequestDto filter, Pageable pageable){
		Date date = new Date();
		String dat = new SimpleDateFormat( "DD.MM.YYYY").format(date);
		String query = "select j \r\n"
				+ " from Job j \r\n"
				+ " where 1=1 \r\n"
			    + " and dataInicio >=   TO_DATE('" + dat + "' , 'DD.MM.YYYYY')  ";
	
		String condicao = " and ";
		if (filter.getStatusJob() != null  && !filter.getStatusJob().isEmpty()) {
			query += condicao + "j.status in (:status) ";
		}
		
		if (filter.getJob() != null  && !filter.getJob().isEmpty()) {
			query += condicao + "j.tipo in (:tipo) ";
		}
		
		query += "order by status, dataProxExec";
		
		var q = em.createQuery(query, Job.class);
		
		if (filter.getStatusJob() != null && !filter.getStatusJob().isEmpty()) {
			q.setParameter("status", filter.getStatusJob());
		}
		
		if (filter.getJob() != null && !filter.getJob().isEmpty()) {
			q.setParameter("tipo", filter.getJob());
		}
		
		return q.getResultList();
	}
	
	public List<Conta> findByContaRequest(ContaSearchRequestDto filter){
		String query = "select c \r\n"
				+ " from Conta c \r\n"
				+ " left join Parceiro p on (p.id = c.parceiro.id) "
				+ " where 1=1 \r\n";
		
	
		String condicao = " and ";
		if (filter.getId() != null) {
			query += condicao + "c.id = :id ";
		}
		
		if (filter.getCodctabco() != null) {
			query += condicao + "c.codctabco = :codctabco ";
		}
		
		if (filter.getStatusapi() != null && !filter.getStatusapi().isBlank()) {
			query += condicao + "c.statusapi = :statusapi ";
		}
		
		if (filter.getRegistrobase() != null ) {
			query += condicao + "c.registrobase = :registrobase ";
		}
		
		if (filter.getNomeParceiro() != null && !filter.getNomeParceiro().isBlank()) {
			query += condicao + "p.nome like :nome ";
		}
		
		if (filter.getConvenio() != null && filter.getConvenio() > 0) {
			query += condicao + "c.convenio = :convenio ";
		}
		
		query += "order by c.id";
		
		var q = em.createQuery(query, Conta.class);
		
		
		if (filter.getId() != null) {
			q.setParameter("id", filter.getId());
		}
		
		if (filter.getCodctabco() != null) {
			q.setParameter("codctabco", filter.getCodctabco());
		}
		
		if (filter.getStatusapi() != null && !filter.getStatusapi().isBlank()) {
			q.setParameter("statusapi", filter.getStatusapi());
		}
		
		if (filter.getRegistrobase() != null ) {
			q.setParameter("registrobase", filter.getRegistrobase());
		}
		
		if (filter.getNomeParceiro() != null && !filter.getNomeParceiro().isBlank()) {
			q.setParameter("nome", '%'+filter.getNomeParceiro()+"%");
		}
		
		if (filter.getConvenio() != null && filter.getConvenio() > 0) {
			q.setParameter("convenio", filter.getConvenio());
		}
		
		return q.getResultList();
	}
	
	public List<QtdStatusDto> findByTotalBolStatus(StatusBoletoFilterSearchRequestDto filter, Pageable pageable){
		String query = "select b.status as status, count(*) as qtd\r\n"
				+ " from Boleto b \r\n"
				+ "	left join Conta c on c.id = b.conta.id \r\n"
				+ "where 1=1 and\r\n"
				+ "	  c.statusapi = 'S' \r\n";
	
		String condicao = " and ";
				
		if (filter.getIdApiBanco() != null && !filter.getIdApiBanco().isEmpty()) {
			query += condicao + "b.conta.id in (:contaId)";
		}
		
		if (filter.getDataVencimentoInicial() != null && !filter.getDataVencimentoInicial().isBlank()) {
			if (filter.getDataVencimentoFinal() != null && !filter.getDataVencimentoFinal().isBlank()) {
				query += condicao + "b.audit.dataUpdated >= TO_DATE(:dataVencimentoInicial , 'DD.MM.YYYYY') and b.audit.dataCreated < TO_DATE(:dataVencimentoFinal , 'DD.MM.YYYYY')";
			} else {
				query += condicao + "b.audit().dataUpdated >= TO_DATE(:dataVencimentoInicial , 'DD.MM.YYYYY')";
			}
		}
		
		query += " group by b.status";
				
		var q = em.createQuery(query, Object[].class);
		
		if (filter.getIdApiBanco() != null && !filter.getIdApiBanco().isEmpty()) {
			q.setParameter("contaId", filter.getIdApiBanco());
		}
		
		if (filter.getDataVencimentoInicial() != null && !filter.getDataVencimentoInicial().isBlank()) {
			q.setParameter("dataVencimentoInicial", filter.getDataVencimentoInicial());
			if (filter.getDataVencimentoFinal() != null && !filter.getDataVencimentoFinal().isBlank()) {
				q.setParameter("dataVencimentoFinal", filter.getDataVencimentoFinal());	
			}
		}
		
		if(pageable != null){
			q.setFirstResult((int) pageable.getOffset());
			q.setMaxResults(pageable.getPageSize());
		}

		List<Object[]> boletos = q.getResultList();
		List<QtdStatusDto> listStatus = new ArrayList<QtdStatusDto>() ;
		for (Object[] objects : boletos) {
			listStatus.add(new QtdStatusDto(((Integer) objects[0]),((Long) objects[1]).intValue()) );
		}
		
		return listStatus;
	}
	
	private String adicionaJoin(String query, StatusBoletoFilterSearchRequestDto filter) {
		String leftJoinContas = " left join Conta c on (b.conta.id = c.id) ";
		String leftJoinPagador = " left join Pagador p on (b.pagador.id = p.id) ";
		String leftJoinNomeClient = " left join parceiros pp on (pp.ID = c.parceiro.id) ";
		
		if (filter.getContas() != null && !filter.getContas().isEmpty()) {	
			query += leftJoinContas;
		}
		
		if (filter.getNumeroInscricaoPagador() != null && !filter.getNumeroInscricaoPagador().isBlank()) {
			query += leftJoinPagador;
		}
		
		if (filter.getNomeCliente() != null && !filter.getNomeCliente().isBlank()) {
			query += leftJoinNomeClient;
		}
		
		return query;
	}
}