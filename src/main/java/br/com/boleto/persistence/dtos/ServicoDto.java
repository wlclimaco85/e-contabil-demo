package br.com.boleto.persistence.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ServicoDto {
	private Integer codserv;
	private String nomeServico;
	private Double valor;
}
