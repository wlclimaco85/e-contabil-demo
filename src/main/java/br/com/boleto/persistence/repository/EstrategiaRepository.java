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
	
	
	@Query(value = "SELECT E.ID,E.ESTRATEGIA,E.DESCRICAO,E.DH_CREATED_AT,E.STATUS,O.TIPO,E.MARGEMACERTO,E.QTDORDENS,E.QTDGAIN,E.QTDLOSS FROM ESTRATEGIAS E , ESTRATEGIAS_POR_ACAO O WHERE E.ID = O.ESTRATEGIAID AND ACAOID = :ACAOID", nativeQuery = true)
	List<Estrategias> findEstrategiasByAcaoId(@Param("ACAOID")  Integer acaoId);
 
}