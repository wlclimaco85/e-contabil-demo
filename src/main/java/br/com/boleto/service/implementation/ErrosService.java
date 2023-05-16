package br.com.boleto.service.implementation;

import java.util.ArrayList;
import java.util.List;

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
		//TODO
		if(filter.getOrdem() != null && filter.getOrdem().getId() > 0) {
			ArrayList<Erros> estrategiaList = errosRepository.findDistinctByErros(filter.getOrdem().getId());
			if(estrategiaList != null && !estrategiaList.isEmpty()) {
				if(estrategiaList.size() == 3) {
					errosRepository.updateStatus(filter.getOrdem().getId());
					resp.setErros(estrategiaList);
					return resp;
				} else {
					return resp.addArray(errosRepository.save(filter));
				}
			} else {
				return resp.addArray(errosRepository.save(filter));
			}
		}
		return resp;
	}
	
	public List<Erros> getErrosAcaoId(Integer acaoId) {
		return errosRepository.findDistinctByErros(acaoId);
	}
	
	public String getErrosAcaoIdByString(Integer acaoId) {
		StringBuffer test = new StringBuffer("");
		boolean isValid = false;
		List<Erros> erros = errosRepository.findDistinctByErros(acaoId);
		for (Erros erros2 : erros) {
			test.append(erros2.getErro());
			test.append(" , ");
			isValid = true;
		}
		if (isValid)
			return test.deleteCharAt(3).toString();
		else 
			return test.toString();
	}
	
}
