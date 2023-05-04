package br.com.boleto.persistence.dtos;

import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ListagemBoletosResponseDto {

	private String indicadorContinuidade;
	private Integer quantidadeRegistros;
	private Integer proximoIndice;
	private ArrayList<ListagemBoletoResponseDto> boletos;
	
}
