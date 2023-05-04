package br.com.boleto.persistence.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.boleto.persistence.entity.Conta;
import br.com.boleto.persistence.entity.Contrato;
import br.com.boleto.persistence.entity.Parceiro;
import br.com.boleto.persistence.entity.Servico;

@Repository
public interface ContratoRepository extends JpaRepository<Contrato, Integer>{
	Optional<Contrato> findByServicoAndParceiro(Servico servico, Parceiro parceiro);
	
	Optional<Contrato> findByServicoAndParceiroAndContaAndAtivo(Servico servico, Parceiro parceiro, Conta conta, String ativo);
	
	Optional<Contrato> findByServicoAndParceiroAndConta(Servico servico, Parceiro parceiro, Conta conta);
}
