package br.com.acoes.persistence.entity;

import java.time.LocalDateTime;

import javax.persistence.CascadeType;
import javax.persistence.Column;
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

import br.com.acoes.persistence.dtos.Acoes3Dto;
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
	@Column(name = "acao")
	private String acaoSigra;
	private String status;
	private String tipo;
	private LocalDateTime dataCompra;
	private LocalDateTime dataVenda;
	private Integer contratos;
	private Double valor;
	private Double valorsuj;
	private Double lucropreju;
	private Double valoracaoatual;
	private Double loss;
	private Double gain;
	private Integer compraAmercado;
	private Integer isPercentualLossGain;
	private Double lossCorrente;
	private Double gainCorrente;
	private Integer tiketId;
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
		this.acaoSigra = acoes.getAcao();
		this.status = estrategia.getStatus();
		this.tipo = estrategia.getTipo();
		if("C".equals(estrategia.getTipo())) {
			this.dataCompra = LocalDateTime.now();
		} else {
			this.dataVenda = LocalDateTime.now();
		}
		this.contratos = estrategia.getContratos();
		this.valor = estrategia.getValor();
		this.loss = estrategia.getLoss();
		this.gain = estrategia.getGain();
		this.compraAmercado = estrategia.getCompraAmercado();
		this.isPercentualLossGain = estrategia.getIsPercentualLossGain();
		this.lossCorrente = estrategia.getLossCorrente();
		this.gainCorrente = estrategia.getGainCorrente();
	}
	
}
