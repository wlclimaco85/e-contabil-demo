package br.com.boleto.persistence.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Acoes5Dto extends Acoes2Dto {
	private String error;
	private Integer qtdBreakeven;
}