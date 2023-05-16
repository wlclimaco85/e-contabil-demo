package br.com.acoes.scheduler;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import br.com.acoes.enums.JobEnum;
import br.com.acoes.persistence.entity.Job;
import br.com.acoes.persistence.repository.JobRepository;
import br.com.acoes.service.implementation.BoletoService;

@Component
public class OperacoesScheduler {

	@Autowired
	BoletoService boletoService;

	@Autowired
	JobRepository jobRepository;

	private final long DEZ = 1000 * 60 * 1;
	private final long VINTE = 1000 * 60 * 10;
	private final long QUARENTA = 1000 * 60 * 40;

//	@Scheduled(fixedDelay = DEZ, initialDelay = 60000)
//	public void registraBoletoQueue() {
//		boletoService.registraBoletoQueue(
//				jobRepository.save(new Job(Timestamp.valueOf(LocalDateTime.now()), JobEnum.REGISTRA.getId(), 0)));
//	}
//
//	@Scheduled(fixedDelay = VINTE, initialDelay = 120000)
//	public void alteraBoletoQueue() {
//		boletoService.alteraBoletoQueue(
//				jobRepository.save(new Job(Timestamp.valueOf(LocalDateTime.now()), JobEnum.ALTERA.getId(), 0)));
//	}
//
//	@Scheduled(fixedDelay = QUARENTA, initialDelay = 240000)
//	public void cancelaBoletoQueue() {
//		boletoService.cancelaBoletoQueue(
//				jobRepository.save(new Job(Timestamp.valueOf(LocalDateTime.now()), JobEnum.CANCELA.getId(), 0)));
//	}

	
	@Scheduled(fixedDelay = DEZ, initialDelay = 60000)
	public void atualizaStatusBoletos() {
		boletoService.atualizaBoletosBaixados(jobRepository
				.save(new Job(Timestamp.valueOf(LocalDateTime.now()), JobEnum.ATUALIZA_STATUS_PCONTA.getId(), 0)));
	}
	
	

}