package br.com.boleto.persistence.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.boleto.persistence.entity.Pagador;

@Repository
public interface PagadorRepository extends JpaRepository<Pagador, Integer> {
	Optional<Pagador> findByNumeroInscricaoAndIdparceiro(String numeroInscricao, Integer idparceiro);
}