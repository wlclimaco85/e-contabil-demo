package br.com.boleto.scheduler;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import br.com.boleto.enums.JobEnum;
import br.com.boleto.persistence.entity.Job;
import br.com.boleto.persistence.repository.JobRepository;
import br.com.boleto.service.implementation.BoletoService;
import br.com.boleto.service.implementation.ContaService;

@Component
public class AtualizaBoletoScheduler {
	@Autowired
	ContaService contaService;

	@Autowired
	BoletoService boletoService;

	private final long UMA_HORA = 1000 * 60 * 1;
	private final long DUAS_HORAS = UMA_HORA * 2;
	
	@Autowired
	JobRepository jobRepository;
	
	@Scheduled(fixedDelay = UMA_HORA, initialDelay = 60000)
	public void atualizaStatusBoletos() {
		boletoService.atualizaBoletosBaixados(jobRepository
				.save(new Job(Timestamp.valueOf(LocalDateTime.now()), JobEnum.ATUALIZA_STATUS_PCONTA.getId(), 0)));
	}
	
	@Scheduled(fixedDelay = DUAS_HORAS, initialDelay = 60000)
	public void atualizaStatusBoletos2() {
		boletoService.atualizarStopLosseGain(jobRepository
				.save(new Job(Timestamp.valueOf(LocalDateTime.now()), JobEnum.ATUALIZA_STATUS_PCONTA.getId(), 0)));
	}

/*
	public void atualizaRetornoBoletoPorDia() {
		boletoService.buscarRetornoBoletoDiario(jobRepository
				.save(new Job(Timestamp.valueOf(LocalDateTime.now()), JobEnum.ATUALIZA_BOLETO.getId(), 0)));
	}


	public void atualizaBoletosPorDia() {
		boletoService.consultaBoletoQueue(jobRepository
				.save(new Job(Timestamp.valueOf(LocalDateTime.now()), JobEnum.ATUALIZA_STATUS_GERAL.getId(), 0)));
	}
*/
	
//	@Scheduled(fixedDelay = DUAS_HORAS, initialDelay = 600000)
//	public void atualizaBoletos() {
//		boletoService.consultaBoletoQueue(jobRepository
//				.save(new Job(Timestamp.valueOf(LocalDateTime.now()), JobEnum.ATUALIZA_STATUS_GERAL.getId(), 0)));
//	}

}