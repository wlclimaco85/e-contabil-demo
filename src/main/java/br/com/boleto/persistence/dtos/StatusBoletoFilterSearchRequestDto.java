package br.com.boleto.persistence.dtos;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StatusBoletoFilterSearchRequestDto {
	
	private List<Integer> status;
	private List<Integer> numeroTituloBeneficiario;
	private List<Integer> idApiBanco;
	private List<String> numeroTituloCliente;
	private List<Integer> statusJob;
	private List<Integer> job;
	private String nomeCliente;
	private String dataVencimentoInicial;
	private String dataVencimentoFinal;
	private String dataEmissaoInicial;
	private String dataEmissaoFinal;
	private String dataLiquidacaoInicial;
	private String dataLiquidacaoFinal;
	private String numeroInscricaoPagador;
	private List<Integer> contas;
}
