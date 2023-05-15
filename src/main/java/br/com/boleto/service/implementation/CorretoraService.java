package br.com.boleto.service.implementation;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.boleto.persistence.dtos.CorretoraResponseDto;
import br.com.boleto.persistence.entity.Corretora;
import br.com.boleto.persistence.mapper.CorretoraMapper;
import br.com.boleto.persistence.repository.CorretoraRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CorretoraService {
	
	@Autowired
	private CorretoraRepository corretorasRepository;

	@Autowired
	private CorretoraMapper corretoraMapper;

	public CorretoraResponseDto insertt(Corretora filter) {
		CorretoraResponseDto aa = new CorretoraResponseDto();
//		Estrategias est = insert(filter);
//		aa.setEstrategias(new EstrategiasDto(est));
		return aa;
	}
	
	public Corretora insert(Corretora filter) {
		//TODO verificar se 
		List<Corretora> estrategiaList = corretorasRepository.findDistinctByCorretoras(filter.getNome());
		if(estrategiaList != null && !estrategiaList.isEmpty()) {
			Corretora est = estrategiaList.get(0);
			return corretorasRepository.save(est);
		} else {
			filter.setStatus("A");
			return corretorasRepository.save(filter);
		}
	}
	
	public List<Corretora> getEstrategias(Integer filter) {
		List<Corretora> list= corretorasRepository.findCorretorasByAcaoId(filter);
		return list == null ? new ArrayList<Corretora>() : list;
	}

	public ArrayList<CorretoraResponseDto>   pesquisaBancoPorId(Integer id) {
		ArrayList<CorretoraResponseDto> list = new  ArrayList<CorretoraResponseDto>();
		for (Corretora estrategiasResponseDto : corretorasRepository.findCorretorasByAcaoId(id)) {
			CorretoraResponseDto bancoResponseDto = new CorretoraResponseDto();
			bancoResponseDto.setCorretora(corretoraMapper.toDtoCorretora(estrategiasResponseDto));
			list.add(bancoResponseDto);
		}
		return list;
	}

	public ArrayList<CorretoraResponseDto> all(Corretora filter) {
		ArrayList<CorretoraResponseDto> bancoResponseDto = new ArrayList<CorretoraResponseDto>();
		List<Corretora> estrategia = corretorasRepository.findAll(); 
		for (Corretora estrategias : estrategia) {
			CorretoraResponseDto dto = new CorretoraResponseDto();
			dto.setCorretora(corretoraMapper.toDtoCorretora(estrategias));
			bancoResponseDto.add(dto);
		}
		return bancoResponseDto;
	}

	public Corretora corretoraByUsuario(Integer id) {
		String usuario = id + "";
		return corretorasRepository.findCorretoraByUsuario(usuario);
	}
	
}
