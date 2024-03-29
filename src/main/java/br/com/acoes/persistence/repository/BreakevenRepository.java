package br.com.acoes.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.acoes.persistence.entity.Breakeven;

@Repository
public interface BreakevenRepository extends JpaRepository<Breakeven, Integer> {

	
	@Query(value = "SELECT * FROM breakevens WHERE status = :status ", nativeQuery = true)
	List<Breakeven> findByStatus(@Param("status")  String status);
	
	@Modifying
	@Query(value = "UPDATE breakevens SET status = :status, erro = :erro  WHERE id = :id", nativeQuery = true)
	void alterarStatus(@Param("status")  String status, @Param("id")  Integer id,@Param("erro")  String erro);
		
	@Query(value = "SELECT * FROM breakevens WHERE status = 'P' and ordem_Id = :acaoId", nativeQuery = true)
	List<Breakeven> findEstrategiasByAcaoId(@Param("acaoId")  Integer acaoId);
 
}