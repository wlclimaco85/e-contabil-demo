package br.com.boleto.persistence.dtos;

import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class StatusBoletoFilterRequestDto {
	
	private List<Integer> status;
	private List<Integer> numeroTituloBeneficiario;
	private List<Integer> idApiBanco;
	private String numeroTituloCliente;
	private String dataVencimentoInicial;
	private String dataVencimentoFinal;
	private String dataEmissaoInicial;
	private String dataEmissaoFinal;
	private String numeroInscricaoPagador;
	private Integer conta;

	@SuppressWarnings("unchecked")
	public StatusBoletoFilterRequestDto(Map<String, Object> statusBoletoFilterRequest) {
		try {
			if (statusBoletoFilterRequest != null) {
				this.status = (List<Integer>) statusBoletoFilterRequest.get("status");
				this.numeroTituloBeneficiario = (List<Integer>) statusBoletoFilterRequest.get("numeroTituloBeneficiario");
				this.idApiBanco = (List<Integer>) statusBoletoFilterRequest.get("idApiBanco");
				this.numeroTituloCliente = (String) statusBoletoFilterRequest.get("numeroTituloCliente");
				this.dataVencimentoInicial = (String) statusBoletoFilterRequest.get("dataVencimentoInicial");
				this.dataVencimentoFinal = (String) statusBoletoFilterRequest.get("dataVencimentoFinal");
				this.dataEmissaoInicial = (String) statusBoletoFilterRequest.get("dataEmissaoInicial");
				this.dataEmissaoFinal = (String) statusBoletoFilterRequest.get("dataEmissaoFinal");
				this.numeroInscricaoPagador = (String) statusBoletoFilterRequest.get("numeroInscricaoPagador");
				this.conta = (Integer) statusBoletoFilterRequest.get("contas");
			}
		} catch (Exception e) {
			//ignored
		}

	}
}
