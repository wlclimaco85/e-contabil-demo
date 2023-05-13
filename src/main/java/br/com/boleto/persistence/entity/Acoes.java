package br.com.boleto.persistence.entity;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import br.com.boleto.persistence.dtos.AcoesDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Entity
@Table(name = "acoes", schema = "public")
@AllArgsConstructor
@NoArgsConstructor
public class Acoes {


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String acao;
	private String status;
	private Double lucropreju;
	private Double valorsuj;
	private String tipo;
	private Integer periodo;
	private String nomeRobo;
	private LocalDateTime data;
	private Double valoracaoatual;
	private String shortname;
	private Integer level;
	private Integer mudouLado;
	private Double loss;
	private Double gain;
	@Embedded
	private Audit audit = new Audit();
	
	@JsonIgnore
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "acao", cascade = CascadeType.ALL)
    private List<EstrategiasPorAcao> atributos;
	
	public Acoes(AcoesDto estrategia) {
		this.id = estrategia.getId();
		this.acao = estrategia.getAcao();
		this.lucropreju = estrategia.getLucropreju();
		this.status = estrategia.getStatus();
		this.valorsuj = estrategia.getValorsuj();
		this.tipo = estrategia.getTipo();
		this.periodo = estrategia.getPeriodo();
		this.nomeRobo = estrategia.getNomeRobo();
		this.data = LocalDateTime.now();
		this.valoracaoatual = estrategia.getValoracaoatual();
		this.shortname = estrategia.getShortname();
		this.level = estrategia.getLevel();
		this.mudouLado = estrategia.getMudouLado();
		this.loss = estrategia.getLoss();
		this.gain = estrategia.getGain();
	}

	public Acoes(Integer id) {
		super();
		this.id = id;
	}
}
