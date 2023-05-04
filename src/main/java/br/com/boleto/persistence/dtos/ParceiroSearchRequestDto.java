package br.com.boleto.persistence.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ParceiroSearchRequestDto {
	
	private String nome;
	private Integer id;
}
