package br.com.boleto.persistence.entity;

import java.time.LocalDateTime;

import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import br.com.boleto.persistence.dtos.Acoes3Dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "ordens", schema = "public")
@AllArgsConstructor
@NoArgsConstructor
public class Ordens {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
//	@OneToOne(targetEntity = Corretora.class, cascade=CascadeType.ALL)
//    @JoinColumn(name = "corretora_id", referencedColumnName = "id")
	@ManyToOne(cascade = CascadeType.MERGE)
	private Corretora corretora;
	
	@OneToOne(targetEntity = Acoes.class, cascade=CascadeType.ALL)
    @JoinColumn(name = "acao_Id", referencedColumnName = "id")
	private Acoes acao;
	
	private String status;
	private Double lucropreju;
	private Double valorsuj;
	private String tipo;
	private LocalDateTime dataCompra;
	private LocalDateTime dataVenda;
	private Integer contratos;
	private Double valoracaoatual;
	private String shortname;
	private Integer mudouLado;
	private Double valor;
	private Double loss;
	private Double gain;
	private Integer compraAmercado;
	private Integer isPercentualLossGain;
	private Double lossCorrente;
	private Double gainCorrente;
	@Embedded
	private Audit audit = new Audit();
	
//	@OneToMany(cascade = CascadeType.ALL, mappedBy = "ordemId")
//    private List<Erros> erros = new ArrayList<Erros>();
//	
//	@OneToMany(cascade = CascadeType.ALL, mappedBy = "ordem_Id")
//    private List<Breakeven> breakevenList = new ArrayList<Breakeven>();
	
	@Transient
	private String error;
	@Transient
	private Integer qtdBreakeven;
	
	public Ordens(Acoes3Dto estrategia,Acoes acoes) {
		this.id = estrategia.getId();
		this.corretora = new Corretora(estrategia.getCorretoraId());
		this.acao = acoes;
		this.status = estrategia.getStatus();
		this.lucropreju = estrategia.getLucropreju();
		this.valorsuj = estrategia.getValor();
		this.tipo = estrategia.getTipo();
		if("C".equals(estrategia.getTipo())) {
			this.dataCompra = LocalDateTime.now();
		} else {
			this.dataVenda = LocalDateTime.now();
		}
		this.contratos = estrategia.getContratos();
		this.valoracaoatual = acoes.getValoracaoatual();
		this.shortname = acoes.getShortname();
		this.mudouLado = estrategia.getMudouLado();
		this.valor = estrategia.getValor();
		this.loss = estrategia.getLoss();
		this.gain = estrategia.getGain();
		this.compraAmercado = estrategia.getCompraAmercado();
		this.isPercentualLossGain = estrategia.getIsPercentualLossGain();
		this.lossCorrente = estrategia.getLossCorrente();
		this.gainCorrente = estrategia.getGainCorrente();
	}
	
}
