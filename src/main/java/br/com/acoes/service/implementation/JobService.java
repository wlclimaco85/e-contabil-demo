package br.com.acoes.service.implementation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.acoes.exception.NotFoundException;
import br.com.acoes.persistence.dtos.JobResponseDto;
import br.com.acoes.persistence.entity.Job;
import br.com.acoes.persistence.repository.JobRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class JobService {
	@Autowired
	private JobRepository jobRepository;
	
	public JobResponseDto pesquisaJobPorStatus(Integer id) {
		JobResponseDto jobResponseDto = new JobResponseDto();
		List<Job> bancoOpt = jobRepository.findByStatus(id);

		if (bancoOpt != null ) {
			jobResponseDto.setJobs(bancoOpt);
			return jobResponseDto;
		}
		throw new NotFoundException("Job não existe na base de dados");
	}
	
	public JobResponseDto pesquisaJobPorTipo(Integer id) {
		JobResponseDto jobResponseDto = new JobResponseDto();
		List<Job> bancoOpt = jobRepository.findByTipo(id);

		if (bancoOpt != null ) {
			jobResponseDto.setJobs(bancoOpt);
			return jobResponseDto;
		}
		throw new NotFoundException("Job não existe na base de dados");
	}

	public JobResponseDto buscaTodosJobs() {
		JobResponseDto jobResponseDto = new JobResponseDto();
		jobResponseDto.setJobs(jobRepository.findAll());

		return jobResponseDto;
	}
}
