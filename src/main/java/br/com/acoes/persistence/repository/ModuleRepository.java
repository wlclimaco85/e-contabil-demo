package br.com.acoes.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.acoes.persistence.entity.Module;

@Repository
public interface ModuleRepository extends JpaRepository<Module, Integer> {
}