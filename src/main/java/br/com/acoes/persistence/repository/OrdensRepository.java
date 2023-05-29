package br.com.acoes.persistence.repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.acoes.persistence.entity.Ordens;

@Repository
public interface OrdensRepository extends JpaRepository<Ordens, Integer> {
	
	ArrayList<Ordens> findByStatus(String status);
	
	@Query(value = "SELECT * FROM Ordens WHERE status = :status and  corretora_id = :corretoraId " , nativeQuery = true)
	List<Ordens> findByStatus(@Param("status")  String status,@Param("corretoraId")  Integer corretoraid);
	
	@Query(value = "SELECT * FROM Ordens WHERE acao_id = :acaoId and  tipo = :acao " , nativeQuery = true)
	List<Ordens> findDistinctByOrdens(@Param("acaoId")  Integer acaoId);
	
	@Query(value = "SELECT * FROM Ordens WHERE acao_id = :acaoId " , nativeQuery = true)
	List<Ordens> findDistinctByAcaoId(@Param("acaoId")  Integer acaoId);
	
	@Query(value = "SELECT * FROM Ordens WHERE acao_id = :acaoId and  tipo = :acao " , nativeQuery = true)
	List<Ordens> findDistinctByOrdens(@Param("acaoId")  Integer acaoId,@Param("acao")  String acao);
	
    @Query(value = "SELECT O.* FROM ORDENS O LEFT JOIN CORRETORAS C ON (O.CORRETORA_ID = C.ID) WHERE C.ID = :acao and o.status = 'D' ORDER BY ID ", nativeQuery = true)
    ArrayList<Ordens> findBuscarOrdensAProcessar(@Param("acao")  Integer acao);
    
    @Query(value = "SELECT * FROM Ordens WHERE id = :acaoId " , nativeQuery = true)
    Ordens findOrdensById(@Param("acaoId")  Integer acaoId);
    
    @Query(value = "SELECT acao FROM Ordens GROUP BY acao ", nativeQuery = true)
	List<String> findDistinctByAcoes();
    
    @Query(value = "SELECT * FROM Ordens WHERE acao like :acao% ", nativeQuery = true)
	List<Ordens> findDistinctByAcoes(@Param("acao")  String acao);
    
    @Modifying
	@Transactional
	@Query(value = "UPDATE Ordens SET valoracaoatual = :valoracaoatual, dh_updated_at = :dh_updated_at WHERE acao like :acao% ",nativeQuery = true)
	void alteraPriceCurrency(@Param("valoracaoatual")  Double valoracaoatual, @Param("dh_updated_at")  LocalDateTime dh_updated_at, @Param("acao")  String acao);
    
    
}