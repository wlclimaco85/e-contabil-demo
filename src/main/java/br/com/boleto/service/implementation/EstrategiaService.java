package br.com.boleto.service.implementation;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.boleto.persistence.dtos.AcoesResponseDto2;
import br.com.boleto.persistence.dtos.EstrategiasDto;
import br.com.boleto.persistence.dtos.EstrategiasResponseDto;
import br.com.boleto.persistence.dtos.EstrategiasResponseDto2;
import br.com.boleto.persistence.entity.Estrategias;
import br.com.boleto.persistence.mapper.EstrategiaMapper;
import br.com.boleto.persistence.repository.EstrategiaRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EstrategiaService {
	
	@Autowired
	private EstrategiaRepository estrategiaRepository;

	@Autowired
	private EstrategiaMapper estrategiaMapper;

	public EstrategiasResponseDto insertt(Estrategias filter) {
		EstrategiasResponseDto aa = new EstrategiasResponseDto();
		Estrategias est = insert(filter);
		aa.setEstrategias(new EstrategiasDto(est));
		return aa;
	}
	
	public Estrategias insert(Estrategias filter) {
		//TODO verificar se 
		List<Estrategias> estrategiaList = estrategiaRepository.findDistinctByEstrategias(filter.getEstrategia());
		if(estrategiaList != null && !estrategiaList.isEmpty()) {
			Estrategias est = estrategiaList.get(0);
			Integer count = (est.getQtdordens() == null ? 0 : est.getQtdordens()) + 1;
			est.setQtdordens(count);
			return estrategiaRepository.save(est);
		} else {
			filter.setStatus("A");
			return estrategiaRepository.save(filter);
		}
	}
	
	public List<Estrategias> getEstrategias(Integer filter) {
		return estrategiaRepository.findEstrategiasByAcaoId(filter);
	}
	
	public String getEstrategiasString(Integer filter) {
		String estrategias = "";
		List<Estrategias> est = getEstrategias(filter);
		for (Estrategias estrategias2 : est) {
			estrategias = estrategias2.getEstrategia() + " ";
		}
		
		return estrategias;
	}

	public ArrayList<EstrategiasResponseDto>   pesquisaBancoPorId(Integer id) {
		ArrayList<EstrategiasResponseDto> list = new  ArrayList<EstrategiasResponseDto>();
		for (Estrategias estrategiasResponseDto : estrategiaRepository.findEstrategiasByAcaoId(id)) {
			EstrategiasResponseDto bancoResponseDto = new EstrategiasResponseDto();
			if(estrategiasResponseDto.getDh_created_at() == null) {
				estrategiasResponseDto.setDh_created_at(LocalDateTime.now());
			}
			bancoResponseDto.setEstrategias(new EstrategiasDto(estrategiasResponseDto));
			list.add(bancoResponseDto);
		}
		return list;
	}

	public ArrayList<EstrategiasResponseDto2> all() {
		ArrayList<EstrategiasResponseDto2> bancoResponseDto = new ArrayList<EstrategiasResponseDto2>();
		List<Estrategias> estrategia = estrategiaRepository.findAll(); 
		for (Estrategias estrategias : estrategia) {
			EstrategiasResponseDto2 dto = new EstrategiasResponseDto2(estrategiaMapper.toDtoEstrategia(estrategias));
			bancoResponseDto.add(dto);
			
		}
		return bancoResponseDto;
	}
	
}
