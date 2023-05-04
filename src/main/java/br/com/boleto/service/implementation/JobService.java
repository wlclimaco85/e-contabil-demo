package br.com.boleto.service.implementation;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.boleto.exception.NotFoundException;
import br.com.boleto.persistence.dtos.JobResponseDto;
import br.com.boleto.persistence.dtos.ListaStatusResponseDto;
import br.com.boleto.persistence.dtos.QtdStatusDto;
import br.com.boleto.persistence.dtos.StatusBoletoFilterSearchRequestDto;
import br.com.boleto.persistence.entity.Job;
import br.com.boleto.persistence.repository.BoletoCustomSearchRepository;
import br.com.boleto.persistence.repository.JobRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class JobService {
	@Autowired
	private JobRepository jobRepository;
	
    @Autowired
    BoletoCustomSearchRepository boletoCustomSearchRepository;

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
	
	public ArrayList<ListaStatusResponseDto> buscaBoletosPaginadosSearchFiltro(StatusBoletoFilterSearchRequestDto filter, Pageable pageable){
        try{
        	ArrayList<ListaStatusResponseDto> boletos = filtraBoletosSearch(filter,pageable);
            return boletos;
        }catch (Exception exception) {
            log.error(exception.getMessage(), exception);
            throw new RuntimeException("Erro ao salvar Parceiro");
        }
    }
	public ArrayList<Job> findByJobRequest(StatusBoletoFilterSearchRequestDto filter, Pageable pageable){
			return (ArrayList<Job>) boletoCustomSearchRepository.findByJobRequest(filter,pageable);
	}
	
	public ArrayList<ListaStatusResponseDto> filtraBoletosSearch(StatusBoletoFilterSearchRequestDto filter, Pageable pageable){

		List<QtdStatusDto> boletolist = boletoCustomSearchRepository.findByTotalBolStatus(filter,pageable);
		List<Job> jobsList =  findByJobRequest(filter,pageable);
		
		ArrayList<ListaStatusResponseDto> list = new ArrayList<ListaStatusResponseDto>();
		ListaStatusResponseDto status = new ListaStatusResponseDto();
		status.setListJobs(jobsList);
		status.setListQtd(boletolist);
		list.add(status);
        return list;
    }
}
