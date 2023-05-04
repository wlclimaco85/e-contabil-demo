package br.com.boleto.persistence.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.boleto.persistence.entity.Servico;

@Repository
public interface ServicoRepository extends JpaRepository<Servico, Integer>{
	Optional<Servico> findByCodserv(Integer codserv);
}
