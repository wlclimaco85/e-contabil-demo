package br.com.boleto.persistence.repository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.boleto.persistence.entity.Conta;
@Repository
public interface ContaRepository extends JpaRepository<Conta, Integer> {
	List<Conta> findByRegistrobaseAndStatusapi(Integer registrobase, String statusapi);

	@Query("SELECT conta FROM Conta conta, Parceiro parceiro WHERE " +
			"conta.codcta =:codCta AND conta.parceiro.id =:idParceiro AND " +
			"conta.registrobase =:registroBase AND conta.parceiro.id = parceiro.id")
	Optional<Conta> buscarContaIdParceiroCodCtaRegistroBase(@Param("codCta") Integer codCta, @Param("idParceiro") Integer idParceiro, @Param("registroBase") Integer registroBase);

	@Modifying
	@Transactional
	@Query(value = "UPDATE contas SET dh_desativacao =:dataDesativacao, statusapi ='N' WHERE id =:idConta",nativeQuery = true)
	void descredenciaConta(@Param("dataDesativacao") LocalDateTime dataDesativacao, @Param("idConta") Integer idConta);
	
	@Modifying
	@Transactional
	@Query(value = "UPDATE contas SET clientid =:clientId, clientsecret =:clientSecret  WHERE id =:idConta",nativeQuery = true)
	void alteraClientIdClientSecret(@Param("clientId") String clientId,@Param("clientSecret") String clientSecret,@Param("idConta") Integer idConta);

	@Modifying
	@Transactional
	@Query(value = "UPDATE contas SET statusapi=:status  WHERE id =:idConta",nativeQuery = true)
	void alteraStatusConta(@Param("status") String status,@Param("idConta") Integer idConta);
	
	@Query("select c from Conta c where c.id in (select distinct conta.id from Boleto where status NOT IN(:status)) and c.statusapi = 'S' ")
	List<Conta> buscarContasQueTemBoletos(@Param("status") List<Integer> status);
	
	@Query(value = "select c.* from contas c where c.id in (select distinct conta_id from boletos where status = :status) and c.statusapi = 'S' ", nativeQuery = true)
	List<Conta> buscarContasQueTemBoletos(@Param("status") Integer status);
	
	@Query("select c from Conta c where c.id in (select distinct conta.id from Boleto where status IN(:status) and dhRecebimentoTitulo >= :dhRecebimentoTitulo) and c.statusapi = 'S' ")
	List<Conta> buscarContasQueTemBoletos(@Param("status") List<Integer> status,@Param("dhRecebimentoTitulo") Timestamp dhRecebimentoTitulo);
	
	@Query(value = "select c.banco_id from contas c where c.id = :contaId", nativeQuery = true)
	Integer buscarIdConta(@Param("contaId") Integer contaId);
	
}