package br.com.boleto.persistence.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.boleto.persistence.entity.LogEnvio;

@Repository
public interface LogEnvioRepository extends JpaRepository<LogEnvio, Integer>{
	List<LogEnvio> findByNossonumeroOrderByDhocorrenciaAsc(String numeroTituloCliente);
	
	@Query("select l from LogEnvio l where l.stacktrace = :stacktrace and l.id = (select max(l2.id) from LogEnvio l2 inner join Boleto b on l2.nossonumero = b.numeroTituloCliente where l2.nossonumero= :numeroTituloCliente)")
	Optional<LogEnvio> findByNossonumeroAndStacktrace(String numeroTituloCliente, String stacktrace);
}
