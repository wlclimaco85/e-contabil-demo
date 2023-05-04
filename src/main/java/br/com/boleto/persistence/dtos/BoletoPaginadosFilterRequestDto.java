package br.com.boleto.persistence.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Getter
@Setter
public class BoletoPaginadosFilterRequestDto {
	private List<Integer> status;

	private List<Integer> numeroTituloBeneficiario;

	private String numeroTituloCliente;

	private String numeroInscricaoPagador;

	private String dataVencimentoInicial;

	private String dataVencimentoFinal;

	private String dataEmissaoInicial;

	private String dataEmissaoFinal;

	private Set<Integer> idApiBanco;

	public BoletoPaginadosFilterRequestDto(List<Integer> status, List<Integer> numeroTituloBeneficiario, String numeroTituloCliente, String numeroInscricaoPagador, String dataVencimentoInicial, String dataVencimentoFinal, String dataEmissaoInicial, String dataEmissaoFinal, Set<Integer> idApiBanco) {
		this.status = status;
		this.numeroTituloBeneficiario = numeroTituloBeneficiario == null ? new ArrayList<>() : numeroTituloBeneficiario;
		this.numeroTituloCliente = numeroTituloCliente == null ? "" : numeroTituloCliente;
		this.numeroInscricaoPagador = numeroInscricaoPagador == null ? "" : numeroInscricaoPagador;
		this.dataVencimentoInicial = dataVencimentoInicial == null ? "" : dataVencimentoInicial;
		this.dataVencimentoFinal = dataVencimentoFinal == null ? "" : dataVencimentoFinal;
		this.dataEmissaoInicial = dataEmissaoInicial == null ? "" : dataEmissaoInicial;
		this.dataEmissaoFinal = dataEmissaoFinal == null ? "" : dataEmissaoFinal;
		this.idApiBanco = idApiBanco;
	}
}
