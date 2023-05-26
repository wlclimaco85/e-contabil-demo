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

import br.com.acoes.persistence.dtos.CorretoraPorOrdemDto;
import br.com.acoes.persistence.entity.Acoes;

@Repository
public interface AcoesRepository extends JpaRepository<Acoes, Integer> {
    ArrayList<Acoes> findByTipo(String ativo);
    ArrayList<Acoes> findByStatus(String status);
    
	@Query(value = "SELECT acao FROM Acoes GROUP BY acao ", nativeQuery = true)
	List<String> findDistinctByAcoes();
	
	@Query(value = "SELECT * FROM Acoes WHERE acao like :acao% ", nativeQuery = true)
	List<Acoes> findDistinctByAcoes(@Param("acao")  String acao);
	
	@Query(value = "select count(o.*) as totalOrdens, (select count(*) from corretoras) as totalCorretoras  from ordens o where o.acao_id = :acaoId", nativeQuery = true)
	CorretoraPorOrdemDto findDistinctByOrdensCorretoras(@Param("acaoId")  Integer acaoId);
	
	@Modifying
	@Transactional
	@Query(value = "UPDATE Acoes SET valoracaoatual = :valoracaoatual, dh_updated_at = :dh_updated_at, shortname = :shortname WHERE acao like :acao% ",nativeQuery = true)
	void alteraPriceCurrency(@Param("valoracaoatual")  Double valoracaoatual, @Param("dh_updated_at")  LocalDateTime dh_updated_at, @Param("shortname")  String shortname, @Param("acao")  String acao);
}