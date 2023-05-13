package br.com.boleto.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.boleto.persistence.entity.Estrategias;

@Repository
public interface EstrategiaRepository extends JpaRepository<Estrategias, Integer> {
	
	@Query(value = "SELECT * FROM estrategias WHERE estrategia like :estrategias ", nativeQuery = true)
	List<Estrategias> findDistinctByEstrategias(@Param("estrategias")  String estrategias);
	
	
	@Query(value = "SELECT E.ID,E.ESTRATEGIA,E.DESCRICAO,E.STATUS,O.TIPO,E.dh_created_at,e.dh_updated_at FROM ESTRATEGIAS E , ESTRATEGIAS_POR_ACAO O WHERE E.ID = O.ESTRATEGIA_ID AND ACAO_ID = :ACAOID", nativeQuery = true)
	List<Estrategias> findEstrategiasByAcaoId(@Param("ACAOID")  Integer acaoId);
 
}