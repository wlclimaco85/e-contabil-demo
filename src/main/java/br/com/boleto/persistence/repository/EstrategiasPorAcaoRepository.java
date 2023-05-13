package br.com.boleto.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.boleto.persistence.entity.EstrategiasPorAcao;

@Repository
public interface EstrategiasPorAcaoRepository extends JpaRepository<EstrategiasPorAcao, Integer> {
	
	
	@Query(value = "SELECT * FROM estrategias_por_acao WHERE acaoId = :acaoId and  estrategiaId = :estrategiaId and  tipo = :tipo", nativeQuery = true)
	List<EstrategiasPorAcao> findDistinctByAcoes(@Param("acaoId")  Integer acaoId,@Param("estrategiaId")  Integer estrategiaId, @Param("tipo")  String tipo);
	
	@Query(value = "SELECT * FROM estrategias_por_acao WHERE acao_Id = :acaoId", nativeQuery = true)
	List<EstrategiasPorAcao> findEstrategiaByAcaoId(@Param("acaoId")  Integer acaoId);
}