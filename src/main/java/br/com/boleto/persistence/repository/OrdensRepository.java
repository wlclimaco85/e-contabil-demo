package br.com.boleto.persistence.repository;

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
}