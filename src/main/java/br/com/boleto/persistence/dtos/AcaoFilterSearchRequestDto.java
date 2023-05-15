package br.com.boleto.persistence.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AcaoFilterSearchRequestDto {
	
	private Integer ambiente;
	private Integer corretoraId;
	private String operacao;
	private String acao;
	private String nomeAcao;
	private String dataIndicacaoInicio;
	private String dataIndicacaoFinal;
	private String nomeEstrategia;
	private String status;
	private double valorInicioInicio;
	private double valorInicioFinal;
	private Integer id;
	private Integer level;
	private Integer mudouLado;
	private Boolean isLucro;
}
