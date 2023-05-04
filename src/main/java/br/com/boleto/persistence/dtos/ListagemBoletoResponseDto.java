package br.com.boleto.persistence.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ListagemBoletoResponseDto {
	
	private String numeroBoletoBB;
	private String dataRegistro;
	private String dataVencimento;
	private Double valorOriginal;
	private Integer carteiraConvenio;
	private Integer variacaoCarteiraConvenio;
	private Integer codigoEstadoTituloCobranca;
	private String estadoTituloCobranca;
	private Integer contrato;
	private String dataMovimento;
	private String dataCredito;
	private Double valorAtual;
	private Double valorPago;

}
