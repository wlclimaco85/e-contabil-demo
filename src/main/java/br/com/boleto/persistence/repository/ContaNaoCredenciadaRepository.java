package br.com.boleto.persistence.repository;

import br.com.boleto.persistence.entity.ContaNaoCredenciada;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface ContaNaoCredenciadaRepository extends JpaRepository<ContaNaoCredenciada, Integer> {

	Optional<ContaNaoCredenciada> findByCodctaAndParceiroIdAndRegistrobase(Integer codCta, Integer idParceiro, Integer registroBase);

	@Transactional
	void deleteByCodctaAndParceiroIdAndRegistrobase(Integer codCta, Integer parceiroId, Integer registroBase);

}