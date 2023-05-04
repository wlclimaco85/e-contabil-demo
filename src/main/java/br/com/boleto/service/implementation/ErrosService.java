package br.com.boleto.service.implementation;

import java.time.LocalDateTime;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.boleto.persistence.dtos.ErrosResponseDto;
import br.com.boleto.persistence.entity.Erros;
import br.com.boleto.persistence.mapper.ErrosMapper;
import br.com.boleto.persistence.repository.ErrosRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ErrosService {
	
	@Autowired
	private ErrosRepository errosRepository;

	@Autowired
	private ErrosMapper estrategiaMapper;

	public ErrosResponseDto insert(Erros filter) {
		ErrosResponseDto resp = new ErrosResponseDto();
		if(filter.getAcaoId() != null && filter.getAcaoId() > 0) {
			ArrayList<Erros> estrategiaList = errosRepository.findDistinctByErros(filter.getAcaoId());
			if(estrategiaList != null && !estrategiaList.isEmpty()) {
				if(estrategiaList.size() == 3) {
					errosRepository.updateStatus(filter.getAcaoId());
					resp.setErros(estrategiaList);
					return resp;
				} else {
					return resp.addArray(errosRepository.save(filter));
				}
			} else {
				filter.setDh_created_at(LocalDateTime.now());
				return resp.addArray(errosRepository.save(filter));
			}
		}
		return resp;
	}
	
}
