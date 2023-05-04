package br.com.boleto.persistence.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.boleto.persistence.entity.BoletoRetorno;

@Repository
public interface BoletoRetornoRepository extends JpaRepository<BoletoRetorno, Integer> {
	Optional<BoletoRetorno> findByIdboleto(Integer idboleto);
}