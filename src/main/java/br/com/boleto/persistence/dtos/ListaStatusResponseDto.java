package br.com.boleto.persistence.dtos;

import java.util.List;

import br.com.boleto.persistence.entity.Job;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ListaStatusResponseDto {
	
	private List<QtdStatusDto> listQtd;
	
	private List<Job> listJobs;
}