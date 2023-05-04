package br.com.boleto.persistence.repository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.boleto.persistence.dtos.BoletoAlteracaoRegistroItauDto;
import br.com.boleto.persistence.dtos.BoletoConsultaItauDto;
import br.com.boleto.persistence.dtos.BoletoPaginadosFilterRequestDto;
import br.com.boleto.persistence.entity.Boleto;

@Repository
public interface BoletoRepository extends JpaRepository<Boleto, Integer>{
	Boleto findBynumeroTituloCliente(String nossonumero);
	
	@Query(value = "select b.* from boletos b \r\n"
			+ "	left join contas c on c.id = b.conta_id\r\n"
			+ "where b.numero_titulo_cliente in (:nossonumero) \r\n"
			+ "	  and c.statusapi = 'S' \r\n"
			+ " order by b.dh_created_at asc", nativeQuery = true)
	Boleto findBynumeroTituloCliente(@Param("nossonumero") List<String> nossonumero);
	
	@Query("select b from Boleto b where status = :status and conta.id = :contaId")
	List<Boleto> findByStatusAndConta(@Param("status")Integer status, @Param("contaId")Integer contaId);

	List<Boleto> findByStatus(Integer status);

	@Query(value = "select * from boletos where status =:status and data_registro_banco + INTERVAL '30' minute <=:dataAtual", nativeQuery = true)
	List<Boleto> findByStatus(@Param("status") Integer status, @Param("dataAtual") Timestamp dataAtual);

	List<Boleto> findDistinctByStatusNotIn(int[] status);

	@Query(value = "select b.* from boletos b \r\n"
			+ "	left join contas c on c.id = b.conta_id\r\n"
			+ "where b.status not in (:status) \r\n"
			+ "	  and c.statusapi = 'S' \r\n"
			+ "	  and c.id = :contaId \r\n"
			+ " order by b.dh_created_at asc", nativeQuery = true)
	List<Boleto> findDistinctByStatusNotIn(@Param("status") List<Integer> status,@Param("contaId") Integer contaId);
	
	@Query(value = "select b.* from boletos b \r\n"
			+ "	left join contas c on c.id = b.conta_id\r\n"
			+ "where b.status not in (:status) \r\n"
			+ "	  and c.statusapi = 'S' \r\n"
			+ " order by b.dh_created_at asc", nativeQuery = true)
	List<Boleto> findDistinctByStatusNotIn(@Param("status") List<Integer> status);
	
	@Query(value = "select b.* from boletos b \r\n"
			+ "	left join contas c on c.id = b.conta_id\r\n"
			+ "where b.numero_titulo_beneficiario in (:numeroTituloBeneficiario) \r\n"
			+ "	  and c.statusapi = 'S' \r\n"
			+ " order by b.dh_created_at asc", nativeQuery = true)
	Boleto findByNumeroTituloBeneficiario(@Param("numeroTituloBeneficiario") List<Integer> numeroTituloBeneficiario);
	
	
	Boleto findByNumeroTituloBeneficiario(Integer numeroTituloBeneficiario);

	List<Boleto> findByNumeroTituloBeneficiarioIsNull();

	@Query("select distinct conta.id from Boleto where status = :status")
	List<Integer> findDistinctByStatus(@Param("status") Integer status);

	@Query(value = "select * from boletos where status =:status and data_registro_banco + INTERVAL '30' minute <=:dataAtual and conta_id =:contaId", nativeQuery = true)
	List<Boleto> findByStatus(@Param("status") Integer status, @Param("dataAtual") Timestamp dataAtual,
			@Param("contaId") Integer contaId);

	@Query(value = "select " +
			"* from boletos b " +
			"where b.status in (:status) " +
			"and b.conta_id = :contaId " +
			"and b.numero_titulo_beneficiario in (:nufins) " +
			"union "+
			"select * from boletos_status_final b " +
			"where b.status in (:status) " +
			"and b.conta_id = :contaId " +
			"and b.numero_titulo_beneficiario in (:nufins) ", nativeQuery = true)
	List<Boleto> findDistinctByStatusIn(@Param("status") Integer[] status, @Param("nufins") Integer[] nufins,
			@Param("contaId") Integer contaId);

	@Query("select distinct conta.id from Boleto where status not in (:status)")
	List<Integer> findByStatusNotIn(@Param("status") int[] status);
	
	@Query("select b from Boleto b where conta.id = :contaId")
	List<Boleto> findByConta(@Param("contaId") Integer contaId);
	
	@Query(value = "select max(dh_updated_at) from boletos where conta_id = :contaId and numero_titulo_beneficiario is not null and dh_updated_at is not null group by id order by dh_updated_at desc limit 1",nativeQuery = true)
	LocalDateTime findUltimaDataAtualizacao(@Param("contaId") Integer contaId);
	
	@Query(value = "select max(dh_created_at) from boletos where conta_id = :contaId and numero_titulo_beneficiario is not null and dh_created_at is not null group by id order by dh_created_at desc limit 1",nativeQuery = true)
	LocalDateTime findUltimaDataCriacao(@Param("contaId") Integer contaId);
	
	@Modifying
	@Transactional
	@Query(value = "UPDATE boletos SET cod_linha_digitavel =:#{#boleto.codigoLinhaDigitavel},status =:#{#boleto.status}, status_banco =:#{#boleto.statusBanco} ,data_registro_banco =:#{#boleto.dataRegistroBanco} WHERE id =:#{#boleto.id}",nativeQuery = true)
	void alteraRegistroBoletoItau(@Param("boleto") BoletoAlteracaoRegistroItauDto boletoAlteracaoRegistroItauDto);

	@Modifying
	@Transactional
	@Query(value = "UPDATE boletos SET status =:#{#boleto.status},status_banco =:#{#boleto.statusBanco} WHERE id =:#{#boleto.id}",nativeQuery = true)
	void alteraConsultaBoletoItau(@Param("boleto") BoletoConsultaItauDto boletoConsultaItauDto);

	List<Boleto> findByStatusInAndConta(int[] status,Integer contaId);
	
	@Query("select b from Boleto b where b.status IN(:status) and b.dhRecebimentoTitulo >= :dhRecebimentoTitulo and b.conta.id = :contaId")
	List<Boleto> buscarBoletosLiquidadosDia(@Param("status") List<Integer> status,@Param("dhRecebimentoTitulo") Timestamp dhRecebimentoTitulo,@Param("contaId") Integer contaId );
	
	@Query("select b from Boleto b where b.conta.id = :contaId and b.status in (:status) order by b.dataRegistro")
	List<Boleto> findByBoletosPendentes(@Param("status") List<Integer> statusLis,@Param("contaId") Integer contaId);

	@Modifying
	@Transactional
	@Query(value = "UPDATE boletos SET status =:status WHERE id =:boletoId",nativeQuery = true)
	void alteraStatusBoleto(@Param("status")  Integer status, @Param("boletoId")  Integer boletoId);

	@Query(value="select b.data_registro_banco from boletos b where b.conta_id = :contaId and status in (:status) order by b.data_registro_banco asc limit 1 ", nativeQuery = true)
	LocalDateTime findDataPrimeiroBoletoRegistrado(@Param("contaId") Integer contaId, @Param("status") List<Integer> status);

	@Query(value="(SELECT * FROM boletos "
			+ " WHERE status IN (:status) "
			+ " AND dh_updated_at > TO_TIMESTAMP(:dtUlConsul,'YYYY-MM-DD HH24:MI:SS.MS') "
			+ " AND conta_id = :contaId "
			+ " UNION "
			+ " SELECT * from boletos_status_final "
			+ " WHERE status IN (:status) "
			+ " AND dh_updated_at > TO_TIMESTAMP(:dtUlConsul,'YYYY-MM-DD HH24:MI:SS.MS') "
			+ " AND conta_id = :contaId) "
			+ " ORDER BY dh_updated_at ASC LIMIT 100 ", nativeQuery = true)
	List<Boleto> findBoletosPorData(@Param("status") List<Integer> status, @Param("contaId") Integer contaId, @Param("dtUlConsul") String dtUlConsul);

	@Query(value = "SELECT boleto.* FROM boletos boleto,pagador pagador " +
			"WHERE boleto.pagador_id = pagador.id " +
			"AND (boleto.conta_id IN(:#{#filtro.idApiBanco})) " +
			"AND (:#{#filtro.status.size()} = 0 OR boleto.status IN(:#{#filtro.status})) " +
			"AND (:#{#filtro.numeroTituloBeneficiario.size()} = 0 OR boleto.numero_titulo_beneficiario IN(:#{#filtro.numeroTituloBeneficiario})) " +
			"AND (NULLIF(:#{#filtro.numeroTituloCliente},'') IS NULL OR boleto.numero_titulo_cliente LIKE CONCAT ('%',:#{#filtro.numeroTituloCliente},'%')) " +
			"AND (NULLIF(:#{#filtro.dataVencimentoInicial},'') IS NULL OR boleto.data_vencimento >= TO_DATE(:#{#filtro.dataVencimentoInicial},'DD-MM-YYYY')) " +
			"AND (NULLIF(:#{#filtro.dataVencimentoFinal},'') IS NULL OR boleto.data_vencimento <= TO_DATE(:#{#filtro.dataVencimentoFinal},'DD-MM-YYYY')) " +
			"AND (NULLIF(:#{#filtro.dataEmissaoInicial},'') IS NULL OR boleto.data_emissao >= TO_DATE(:#{#filtro.dataEmissaoInicial},'DD-MM-YYYY')) " +
			"AND (NULLIF(:#{#filtro.dataEmissaoFinal},'') IS NULL OR boleto.data_emissao <= TO_DATE(:#{#filtro.dataEmissaoFinal},'DD-MM-YYYY')) " +
			"AND (NULLIF(:#{#filtro.numeroInscricaoPagador},'') IS NULL OR pagador.numero_inscricao = :#{#filtro.numeroInscricaoPagador}) " +
			" UNION " +
			"SELECT boleto.* FROM boletos_status_final boleto, pagador pagador " +
			"WHERE boleto.pagador_id = pagador.id " +
			"AND (boleto.conta_id IN(:#{#filtro.idApiBanco})) " +
			"AND (:#{#filtro.status.size()} = 0 OR boleto.status IN(:#{#filtro.status})) "+
			"AND (:#{#filtro.numeroTituloBeneficiario.size()} = 0 OR boleto.numero_titulo_beneficiario IN(:#{#filtro.numeroTituloBeneficiario})) " +
			"AND (NULLIF(:#{#filtro.numeroTituloCliente},'') IS NULL OR boleto.numero_titulo_cliente LIKE CONCAT ('%',:#{#filtro.numeroTituloCliente},'%')) "+
			"AND (NULLIF(:#{#filtro.dataVencimentoInicial},'') IS NULL OR boleto.data_vencimento >= TO_DATE(:#{#filtro.dataVencimentoInicial},'DD-MM-YYYY')) " +
			"AND (NULLIF(:#{#filtro.dataVencimentoFinal},'') IS NULL OR boleto.data_vencimento <= TO_DATE(:#{#filtro.dataVencimentoFinal},'DD-MM-YYYY')) "+
			"AND (NULLIF(:#{#filtro.dataEmissaoInicial},'') IS NULL OR boleto.data_emissao >= TO_DATE(:#{#filtro.dataEmissaoInicial},'DD-MM-YYYY')) " +
			"AND (NULLIF(:#{#filtro.dataEmissaoFinal},'') IS NULL OR boleto.data_emissao <= TO_DATE(:#{#filtro.dataEmissaoFinal},'DD-MM-YYYY')) "+
			"AND (NULLIF(:#{#filtro.numeroInscricaoPagador},'') IS NULL OR pagador.numero_inscricao = :#{#filtro.numeroInscricaoPagador})",nativeQuery = true)
	Page<Boleto> boletosPaginadosFiltro(@Param("filtro") BoletoPaginadosFilterRequestDto filtro, Pageable pageable);

	boolean existsByContaId(Integer idConta);

	@Query(value = "select c.banco_id from boletos b inner join contas c on b.conta_id = c.id "
			+ "where b.numero_titulo_cliente = :numeroTituloCliente", nativeQuery = true)
	Integer buscaBancoPorNumeroTituloCliente(@Param("numeroTituloCliente")String numeroTituloCliente);
}