package br.com.boleto.persistence.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public @NoArgsConstructor class RegistroBoletoErroDto {
	private String codigo;
	private String versao;
	private String mensagem;
	private String ocorrencia;
	
}
