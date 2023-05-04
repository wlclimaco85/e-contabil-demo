package br.com.boleto.persistence.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.boleto.persistence.entity.Certificado;

@Repository
public interface CertificadoRepository extends JpaRepository<Certificado, Integer> {
	@Query("SELECT certificado FROM Certificado certificado WHERE certificado.conta.id =:idConta")
	Optional<Certificado> buscaCertificadoConta(@Param("idConta") Integer idConta);
}