package br.com.boleto.persistence.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContaSearchRequestDto {
	private Integer id;
	private Integer codctabco;
	private Integer convenio;
	private String statusapi;
	private Integer registrobase;
	private String nomeParceiro;
}
