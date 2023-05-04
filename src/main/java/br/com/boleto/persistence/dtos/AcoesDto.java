package br.com.boleto.persistence.dtos;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AcoesDto {
	private Integer id;
	private String acao;
	private LocalDateTime dh_created_at;
	private String status;
	private Double lucropreju;
	private Double valorsuj;
	private String tipo;
	private Integer periodo;
	private Integer ambiente;
	private String nomeRobo;
	private LocalDateTime dataCompra;
	private LocalDateTime dataVenda;
	private Integer contratos;
	private Double valoracaoatual;
	private LocalDateTime dh_updated_at;
	private String shortname;
	private Integer level;
	private Integer mudouLado;
	private Double valor;
	private Integer acaoOrigem;
	
	
	public AcoesDto(Acoes2Dto estrategia) {
		this.id = estrategia.getId();
		this.acao = estrategia.getAcao();
		this.dh_created_at = estrategia.getDh_created_at() == null ? LocalDateTime.now() : estrategia.getDh_created_at();
		this.status = estrategia.getStatus();
		this.valorsuj = estrategia.getValorcompra();
		this.tipo = estrategia.getTipo();
		this.periodo = estrategia.getPeriodo();
		this.ambiente = "T".equals(estrategia.getAmbiente()) ? 0 : 1;
		if("C".equals(tipo)) {
			this.dataCompra = LocalDateTime.now();
		} else {
			this.dataVenda = LocalDateTime.now();
		}
		this.contratos = estrategia.getContratos();
	}

}