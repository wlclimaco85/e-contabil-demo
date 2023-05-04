package br.com.boleto.persistence.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ParceiroDto {
	private Integer id;
	private String nome;
}
