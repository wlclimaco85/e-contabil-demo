package br.com.acoes.persistence.dtos;

import java.util.List;

import br.com.acoes.persistence.entity.Job;
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