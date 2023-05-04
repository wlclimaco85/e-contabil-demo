package br.com.boleto.persistence.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AlteracaoBoletoResponseDto {
	private Long numeroContratoCobranca;

	private String dataAtualizacao;

	private String horarioAtualizacao;
}
