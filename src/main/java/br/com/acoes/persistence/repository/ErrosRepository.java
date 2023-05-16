package br.com.acoes.persistence.repository;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.acoes.persistence.entity.Erros;

@Repository
public interface ErrosRepository extends JpaRepository<Erros, Integer> {
	
	@Query(value = "SELECT * FROM erros WHERE ordem_Id = :acaoId ", nativeQuery = true)
	ArrayList<Erros> findDistinctByErros(@Param("acaoId")  Integer acaoId);
	
	@Query(value = "UPDATE ACOES SET STATUS = 'D' WHERE ID = :acaoId", nativeQuery = true)
	void updateStatus(@Param("acaoId")  Integer acaoId);
 
}