package br.com.boleto.service.implementation;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.boleto.persistence.dtos.BreakevenDto;
import br.com.boleto.persistence.entity.Breakeven;
import br.com.boleto.persistence.mapper.BreakevenMapper;
import br.com.boleto.persistence.repository.BreakevenRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class BreakevenService {
	
	@Autowired
	private BreakevenRepository breakevenRepository;

	@Autowired
	private BreakevenMapper breakevenMapper;

	public Breakeven insert(Breakeven filter) {
		Breakeven aa = new Breakeven();
		aa = breakevenRepository.save(filter);
		return aa;
	}
	
	public List<Breakeven> insert(List<Breakeven> filter) {
		List<Breakeven> aa = new ArrayList<Breakeven>();
		aa = breakevenRepository.saveAll(filter);
		return aa;
	}
	
	public Breakeven alterar(Breakeven filter) {
		return breakevenRepository.save(filter);
	}
	
	public List<Breakeven> alterar(List<Breakeven> filter) {
		return breakevenRepository.saveAll(filter);
	}
	
	public List<BreakevenDto> findByStatus(String status) {
		return  breakevenMapper.toDtoListBreakeven(breakevenRepository.findByStatus(status));
	}
	
	
}
