package br.com.acoes.persistence.dtos;

import java.time.LocalDateTime;

import br.com.acoes.persistence.entity.Acoes;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OrdensDto {
	
	private Integer id;
	private Integer acaoId;
	private String tipo;
	private Integer contratos;
	private LocalDateTime dh_created_at;
	private String status;
	private Double valorcompra;
	private Double valorvenda;
	private Integer ambiente;
	private LocalDateTime dh_compra_at;
	private LocalDateTime dh_venda_at;
	private Double loss;
	private Double gain;
	private Integer contratosCorrentes;
	
	
	public OrdensDto(Acoes acoes) {
//		this.id = acoes.getId();
//		this.acao = acoes.getAcao();
//		//this.estrategia = acoes.getEstrategia(); //TODO
//		this.dh_created_at = acoes.getDh_created_at();
//		this.status = acoes.getStatus();
//		this.valorcompra = acoes.getValorcompra();
//		this.loss = acoes.getLoss();
//		this.gain = acoes.getGain();
//		this.valorcomprado = acoes.getValorcomprado();
//		this.tipo = acoes.getTipo();
//		this.periodo = acoes.getPeriodo();
//		this.ambiente = acoes.getAmbiente();
//		this.nomeRobo = acoes.getNomeRobo();
//		this.dataVenda = acoes.getDataVenda();
//		this.contratos = acoes.getContratos();
//		this.valoracaoatual = acoes.getValoracaoatual();
//		this.dh_updated_at = LocalDateTime.now();
//		this.shortname = acoes.getShortname();
	}
}