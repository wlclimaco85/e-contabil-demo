package br.com.boleto.persistence.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class BoletoPaginadosDto {
	private Integer numeroTituloBeneficiario;

	private String numeroTituloCliente;

	private Integer numeroConvenio;

	private Integer status;

	private String descricaoStatus;

	private Integer idapibanco;

	private String dataVencimento;

	private String dataEmissao;

	private Double valorOriginal;
}
