package br.com.boleto.service.implementation;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.boleto.exception.NotFoundException;
import br.com.boleto.persistence.dtos.ParceiroResponseDto;
import br.com.boleto.persistence.dtos.ParceiroSearchRequestDto;
import br.com.boleto.persistence.entity.Parceiro;
import br.com.boleto.persistence.repository.BoletoCustomSearchRepository;
import br.com.boleto.persistence.repository.ParceiroRepository;

@Service
public class ParceiroService {
	@Autowired
	private ParceiroRepository parceiroRepository;

	@Autowired
	BoletoCustomSearchRepository boletoCustomSearchRepository;

	public ParceiroResponseDto pesquisaParceiroPorStatus(Integer id) {
		ParceiroResponseDto parceiroResponseDto = new ParceiroResponseDto();
		Optional<Parceiro> parceiroOpt = parceiroRepository.findById(id);

		if (parceiroOpt.isPresent()) {
			parceiroResponseDto.setParceiros(new ArrayList<>());
			parceiroResponseDto.getParceiros().add(parceiroOpt.get());
			return parceiroResponseDto;
		}
		throw new NotFoundException("Parceiro não existe na base de dados");
	}

	public ArrayList<Parceiro> findByParceiroRequest(ParceiroSearchRequestDto filter, Pageable pageable) {
		return (ArrayList<Parceiro>) boletoCustomSearchRepository.findByParceiroRequest(filter);
	}
	
	public void salva(Parceiro parceiro) {
		parceiroRepository.save(parceiro);
	}

}
