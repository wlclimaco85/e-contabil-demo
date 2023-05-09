package br.com.boleto.service.implementation;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.boleto.persistence.dtos.OrdensResponseDto;
import br.com.boleto.persistence.entity.Ordens;
import br.com.boleto.persistence.mapper.OrdensMapper;
import br.com.boleto.persistence.repository.OrdensRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class OrdensService {
	
	@Autowired
	private OrdensRepository estrategiaRepository;
	
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
	
}
