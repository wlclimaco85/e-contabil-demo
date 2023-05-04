package br.com.boleto.persistence.dtos;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LogEnvioSearchRequestDto {
	
	private Integer numeroTituloBeneficiario;
	private Integer idApiBanco;
	private String numeroTituloCliente;
	private List<Integer> status;
	private List<Integer> tipoEvento;
	private List<Integer> situacao;
	private String dataOcorrenciaInicial;
	private String dataOcorrenciaFinal;
}
