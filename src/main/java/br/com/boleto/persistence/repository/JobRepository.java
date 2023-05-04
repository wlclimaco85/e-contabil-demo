package br.com.boleto.persistence.repository;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.boleto.persistence.entity.Job;

@Repository
public interface JobRepository extends JpaRepository<Job, Integer> {
    ArrayList<Job> findByTipo(Integer ativo);
	
    ArrayList<Job> findByStatus(Integer status);
}