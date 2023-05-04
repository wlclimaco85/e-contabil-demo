package br.com.boleto.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.boleto.persistence.entity.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
}