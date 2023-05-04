package br.com.boleto.persistence.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.boleto.persistence.entity.Termo;

@Repository
public interface TermoRepository extends JpaRepository<Termo, Integer>{
	@Query("select t from Termo t where t.versao = (select max(t2.versao) from Termo t2)")
	Optional<Termo> findAtual();
}
