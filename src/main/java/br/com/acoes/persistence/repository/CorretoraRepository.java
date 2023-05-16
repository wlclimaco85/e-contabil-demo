package br.com.acoes.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.acoes.persistence.entity.Corretora;

@Repository
public interface CorretoraRepository extends JpaRepository<Corretora, Integer> {
	
	@Query(value = "SELECT * FROM corretoras WHERE nome like :estrategias ", nativeQuery = true)
	List<Corretora> findDistinctByCorretoras(@Param("estrategias")  String estrategias);
	
	
	@Query(value = "SELECT * FROM corretoras E WHERE E.ID = :ACAOID", nativeQuery = true)
	List<Corretora> findCorretorasByAcaoId(@Param("ACAOID")  Integer acaoId);
	
	@Query(value = "SELECT * FROM corretoras E WHERE E.USUARIOMQ5 = :USUARIOMQ5", nativeQuery = true)
	Corretora findCorretoraByUsuario(@Param("USUARIOMQ5")  String acaoId);
 
}