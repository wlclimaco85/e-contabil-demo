package br.com.boleto.persistence.dtos;

import java.time.LocalDateTime;

import br.com.boleto.persistence.entity.Audit;
import br.com.boleto.persistence.entity.Ordens;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Ordens2Dto {
	private Integer id;
	private Integer corretoraId;
	private String acao;
	private String estrategia;
	private LocalDateTime dh_created_at;
	private String status;
	private Double valorcompra;
	private Double loss;
	private Double gain;
	private Double valorcomprado;
	private String tipo;
	private Integer periodo;
	private Integer qtdEstrategia;
	private String ambiente;
	private String nomeRobo;
	private String dataVenda;
	private Integer contratos;
	private Integer level;
	private Integer mudouLado;
	private Double valoracaoatual;
	private LocalDateTime dh_updated_at;
	private String shortname;
	private Integer compraAmercado;
	private Double lucropreju;
	private Double valorsuj;
	private String dataCompra;
	private Double valor;
	private Integer acaoOrigem;
	private Integer isPercentualLossGain;
	private Double lossCorrente;
	private Double gainCorrente;
	private String error;
	private Integer qtdBreakeven;
	private Audit audit = new Audit();
	
	
	public Ordens2Dto(Ordens estrategia) {
		this.id = estrategia.getId();
		this.acao = estrategia.getAcao().getAcao();
		this.lucropreju = estrategia.getLucropreju();
		this.status = estrategia.getStatus();
		this.valorsuj = estrategia.getValorsuj();
		this.tipo = estrategia.getTipo();
		this.periodo = 0;
		this.nomeRobo = estrategia.getAcao().getNomeRobo();
		this.valoracaoatual = estrategia.getValoracaoatual();
		this.shortname = estrategia.getShortname();
		this.mudouLado = estrategia.getMudouLado();
		this.loss = estrategia.getLoss();
		this.gain = estrategia.getGain();
		this.corretoraId = estrategia.getCorretora().getId();
		this.dh_created_at =  estrategia.getAudit() != null ? estrategia.getAudit().getDataCreated() :  LocalDateTime.now();
		this.valorcompra = estrategia.getValorsuj();
		this.valorcomprado = estrategia.getValorsuj();
		this.contratos = estrategia.getContratos();
		this.level = estrategia.getAcao().getLevel();
		this.mudouLado = estrategia.getAcao().getMudouLado();
		this.dh_updated_at = estrategia.getAudit() != null ? estrategia.getAudit().getDataUpdated() :  LocalDateTime.now();;
		this.compraAmercado = estrategia.getCompraAmercado();
		this.valor = estrategia.getValorsuj();
		this.isPercentualLossGain  = estrategia.getIsPercentualLossGain();
		this.lossCorrente  = estrategia.getLossCorrente();
		this.gainCorrente  = estrategia.getGainCorrente();
	}
}