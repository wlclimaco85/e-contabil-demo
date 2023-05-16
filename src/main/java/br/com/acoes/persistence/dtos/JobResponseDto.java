package br.com.acoes.persistence.dtos;

import java.util.List;

import br.com.acoes.persistence.entity.Job;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JobResponseDto {
	private List<Job> jobs;
}
