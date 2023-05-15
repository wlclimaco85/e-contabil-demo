package br.com.boleto.persistence.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.boleto.persistence.entity.Ordens;

@Repository
public interface OrdensRepository extends JpaRepository<Ordens, Integer> {
	
	@Query(value = "SELECT * FROM Ordens WHERE acaoid = :acaoId and  tipo = :acao " , nativeQuery = true)
	List<Ordens> findDistinctByOrdens(@Param("acaoId")  Integer acaoId);
	
	@Query(value = "SELECT * FROM Ordens WHERE acaoid = :acaoId and  tipo = :acao " , nativeQuery = true)
	List<Ordens> findDistinctByOrdens(@Param("acaoId")  Integer acaoId,@Param("acao")  String acao);
	
    @Query(value = "SELECT O.* FROM ORDENS O LEFT JOIN CORRETORAS C ON (O.CORRETORA_ID = C.ID) WHERE C.ID = :acao ORDER BY ID ", nativeQuery = true)
    ArrayList<Ordens> findBuscarOrdensAProcessar(@Param("acao")  Integer acao);
    
    @Query(value = "SELECT * FROM Ordens WHERE id = :acaoId " , nativeQuery = true)
    Ordens findOrdensById(@Param("acaoId")  Integer acaoId);
    
}