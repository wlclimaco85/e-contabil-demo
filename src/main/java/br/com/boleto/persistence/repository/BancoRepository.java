package br.com.boleto.persistence.repository;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.boleto.persistence.entity.Banco;

@Repository
public interface BancoRepository extends JpaRepository<Banco, Integer> {
    ArrayList<Banco> findByAtivo(boolean ativo);
}