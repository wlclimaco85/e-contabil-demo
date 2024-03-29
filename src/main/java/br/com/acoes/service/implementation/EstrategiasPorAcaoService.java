package br.com.acoes.service.implementation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.acoes.persistence.entity.EstrategiasPorAcao;
import br.com.acoes.persistence.mapper.EstrategiasPorAcaoMapper;
import br.com.acoes.persistence.repository.EstrategiasPorAcaoRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EstrategiasPorAcaoService {
	@Autowired
	private EstrategiasPorAcaoRepository estrategiaRepository;

	@Autowired
	private EstrategiasPorAcaoMapper estrategiaMapper;

	
	public EstrategiasPorAcao insert(EstrategiasPorAcao filter) {
		return estrategiaRepository.save(filter);
	}
	
	public List<EstrategiasPorAcao> isExitEstretegia(EstrategiasPorAcao filter) {
		//TODO verificar se 
	//	List<EstrategiasPorAcao> estrategiaList = estrategiaRepository.findDistinctByAcoes(filter.getAcaoid(),filter.getEstrategiaid(),filter.getTipo());
//		if(estrategiaList != null && !estrategiaList.isEmpty()) {
//			return estrategiaList;
//		} else {
//			return null;
//		}
		return null;
	}

	public List<EstrategiasPorAcao> findEstrategiaByAcaoId(Integer id) {
		return estrategiaRepository.findEstrategiaByAcaoId(id);
	}
}
