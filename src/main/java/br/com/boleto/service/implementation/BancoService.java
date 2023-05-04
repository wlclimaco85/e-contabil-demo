package br.com.boleto.service.implementation;

import static java.util.Objects.isNull;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.boleto.exception.NotFoundException;
import br.com.boleto.persistence.dtos.AcoesDto;
import br.com.boleto.persistence.dtos.BancoResponseDto;
import br.com.boleto.persistence.entity.Banco;
import br.com.boleto.persistence.mapper.BancoMapper;
import br.com.boleto.persistence.repository.BancoRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class BancoService {
	@Autowired
	private BancoRepository bancoRepository;

	@Autowired
	private BancoMapper bancoMapper;

	public BancoResponseDto pesquisaBancoPorId(Integer id) {
		ArrayList<AcoesDto> bancos = new ArrayList<>();
		BancoResponseDto bancoResponseDto = new BancoResponseDto();
		Optional<Banco> bancoOpt = bancoRepository.findById(id);

		if (bancoOpt.isPresent()) {
			bancos.add(bancoMapper.toDtoBanco(bancoOpt.get()));
			bancoResponseDto.setBanco(bancos);
			return bancoResponseDto;
		}
		log.info("Banco não existe na base de dados - Id: {} [Método: pesquisar]", isNull(id) ? "Nulo" : id.toString());
		throw new NotFoundException("Banco não existe na base de dados");
	}

	public BancoResponseDto buscaTodosBancos() {
		BancoResponseDto bancoResponseDto = new BancoResponseDto();
		bancoResponseDto.setBanco(bancoMapper.toDtoListBanco(bancoRepository.findByAtivo(true)));

		return bancoResponseDto;
	}
}
