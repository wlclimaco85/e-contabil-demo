package br.com.boleto.persistence.dtos;

import java.util.List;

import br.com.boleto.persistence.entity.Job;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JobResponseDto {
	private List<Job> jobs;
}
