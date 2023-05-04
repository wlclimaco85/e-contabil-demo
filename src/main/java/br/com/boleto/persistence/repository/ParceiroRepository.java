package br.com.boleto.persistence.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.boleto.persistence.entity.Parceiro;

@Repository
public interface ParceiroRepository extends JpaRepository<Parceiro, Integer> {
	
    Optional<Parceiro> findById(Integer ativo);
}