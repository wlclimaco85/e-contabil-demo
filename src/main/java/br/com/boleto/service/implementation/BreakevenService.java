package br.com.boleto.service.implementation;

import java.lang.StackWalker.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

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

	public BreakevenDto insert(Breakeven filter) {
		return breakevenMapper.toDtoBreakeven(breakevenRepository.save(filter));
	}
	
	public List<Breakeven> insert(List<Breakeven> filter) {
		return breakevenRepository.saveAll(filter);
	}
	@Transactional
	public BreakevenDto alterar(Breakeven filter) {
		try {
			breakevenRepository.alterarStatus(filter.getStatus(),filter.getId(),filter.getErro());
			Optional<Breakeven> op = breakevenRepository.findById(filter.getId());
			if(op != null)
				return breakevenMapper.toDtoBreakeven(op.get());
		} catch (Exception e) {
			// TODO: handle exception
			return new BreakevenDto();
		}
		return new BreakevenDto();
	}
	
	public List<Breakeven> alterar(List<Breakeven> filter) {
		return breakevenRepository.saveAll(filter);
	}
	
	public ArrayList<BreakevenDto> findByStatus(String status) {
		return  breakevenMapper.toDtoListBreakeven(breakevenRepository.findByStatus(status));
	}
	
	public ArrayList<BreakevenDto> findByAcaoId(Integer status) {
		ArrayList<BreakevenDto> list = breakevenMapper.toDtoListBreakeven(breakevenRepository.findEstrategiasByAcaoId(status));
		return  list != null ? list : new ArrayList<BreakevenDto>();
	}
	
	
}
