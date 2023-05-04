package br.com.boleto.persistence.repository;

import br.com.boleto.persistence.entity.BoletoStatusFinal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoletoStatusFinalRepository extends JpaRepository<BoletoStatusFinal, Integer> {

    boolean existsByContaId(Integer idConta);

}