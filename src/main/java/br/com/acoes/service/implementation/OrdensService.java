package br.com.acoes.service.implementation;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.acoes.persistence.dtos.Acoes2Dto;
import br.com.acoes.persistence.dtos.AcoesResponseDto2;
import br.com.acoes.persistence.dtos.OrdensResponseDto;
import br.com.acoes.persistence.entity.Breakeven;
import br.com.acoes.persistence.entity.Ordens;
import br.com.acoes.persistence.mapper.OrdensMapper;
import br.com.acoes.persistence.repository.BreakevenRepository;
import br.com.acoes.persistence.repository.OrdensRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class OrdensService {
	
	@Autowired
	private OrdensRepository estrategiaRepository;
	
	@Autowired
	private BreakevenRepository breakevenRepository;
	
	@Autowired
	private OrdensMapper ordensMapper;
	
	public ArrayList<OrdensResponseDto>   pesquisaBancoPorId(Integer id) {
		ArrayList<OrdensResponseDto> list = new  ArrayList<OrdensResponseDto>();
		for (Ordens estrategiasResponseDto : estrategiaRepository.findDistinctByOrdens(id)) {
			OrdensResponseDto bancoResponseDto = new OrdensResponseDto();
			bancoResponseDto.setOrdens(ordensMapper.toDtoOrdens(estrategiasResponseDto));
			list.add(bancoResponseDto);
		}
		return list;
	}
	
	public ArrayList<OrdensResponseDto> all() {
		ArrayList<OrdensResponseDto> bancoResponseDto = new ArrayList<OrdensResponseDto>();
		List<Ordens> estrategia = estrategiaRepository.findAll(); 
		for (Ordens estrategias : estrategia) {
			OrdensResponseDto dto = new OrdensResponseDto(ordensMapper.toDtoOrdens(estrategias));
			bancoResponseDto.add(dto);
		}
		return bancoResponseDto;
	}
	
	public Ordens getAcaoById(Integer id) {
		return estrategiaRepository.findOrdensById(id);
	}

	@Transactional
	public String compraVender(Ordens ordens,Double price,Double sl,Double sg) {
		String msg = "Ordens realizada com sucesso!";
		try {
			ordens.setStatus("A");
			ordens.setValor(price);
			ordens.setGain(sg);
			ordens.setLoss(sl);
			ordens.setGainCorrente(sg);
			ordens.setLossCorrente(sl);
			Ordens orde = estrategiaRepository.save(ordens);
			Breakeven breakean = new Breakeven();
			breakean.setOrdem(orde);
			breakean.setGainAtual(sg);
			breakean.setLossAtual(sl);
			breakean.setStatus("A");
			breakean.setAcao(orde.getAcaoSigra());
			breakean.setValorAtualAcao(price);
			breakevenRepository.save(breakean);
		} catch (Exception e) {
			msg = e.getMessage();
		}
		return msg;
	}
	
	public ArrayList<AcoesResponseDto2>  findByStatus(String tipo,Integer corretoraId) {
		ArrayList<AcoesResponseDto2> bancoResponseDto = new ArrayList<AcoesResponseDto2>();
		if(corretoraId == null || corretoraId == 0) {
			for (Ordens acoesResponseDto : estrategiaRepository.findByStatus(tipo)) {
				Acoes2Dto acoes = new Acoes2Dto(acoesResponseDto);
				AcoesResponseDto2 response = new AcoesResponseDto2();
				response.setBanco(acoes);
				bancoResponseDto.add(response);
			}
		} else {
			for (Ordens acoesResponseDto : estrategiaRepository.findByStatus(tipo,corretoraId)) {
				Acoes2Dto acoes = new Acoes2Dto(acoesResponseDto);
				AcoesResponseDto2 response = new AcoesResponseDto2();
				response.setBanco(acoes);
				bancoResponseDto.add(response);
			}
		}	
		return bancoResponseDto;
	}
	
}
