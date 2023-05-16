package br.com.boleto.persistence.dtos;

import java.time.LocalDateTime;

import javax.persistence.Embedded;

import br.com.boleto.persistence.entity.Audit;
import br.com.boleto.persistence.entity.Corretora;
import br.com.boleto.persistence.entity.Ordens;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AcoesDto {
	private Integer id;
	private Integer acaoId;
	private Corretora corretora;
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
	
	public AcoesDto(AcoesDto estrategia) {
		this.id = estrategia.getId();
		this.corretora = estrategia.getCorretora();
		this.acao = estrategia.getAcao();
		this.acaoId = estrategia.getAcaoId();
		this.lucropreju = estrategia.getLucropreju();
		this.valorsuj = estrategia.getValorsuj();
		this.tipo = estrategia.getTipo();
		this.nomeRobo = estrategia.getNomeRobo();
		this.data = LocalDateTime.now();
		this.valoracaoatual = estrategia.getValoracaoatual();
		this.shortname = estrategia.getShortname();
		this.level = estrategia.getLevel();
		this.mudouLado = estrategia.getMudouLado();
		this.status = estrategia.getStatus();
		this.loss = estrategia.getLoss();
		this.gain = estrategia.getGain();
	}
	public AcoesDto(Acoes2Dto estrategia) {
		this.id = estrategia.getId();
		this.corretora = new Corretora(estrategia.getCorretoraId());
		this.acao = estrategia.getAcao();
		this.acaoId = estrategia.getAcaoId();
		this.lucropreju = estrategia.getLucropreju();
		this.valorsuj = estrategia.getValorsuj();
		this.tipo = estrategia.getTipo();
		this.nomeRobo = estrategia.getNomeRobo();
		this.data = LocalDateTime.now();
		this.valoracaoatual = estrategia.getValoracaoatual();
		this.shortname = estrategia.getShortname();
		this.level = estrategia.getLevel();
		this.mudouLado = estrategia.getMudouLado();
		this.status = estrategia.getStatus();
		this.loss = estrategia.getLoss();
		this.gain = estrategia.getGain();
	}
	
	public AcoesDto(Ordens estrategia) {
		this.id = estrategia.getId();
		this.corretora = estrategia.getCorretora();
		this.acao = estrategia.getAcao().getAcao();
		this.acaoId = estrategia.getAcao().getId();
		this.lucropreju = 0.0;
		this.valorsuj = estrategia.getValor();
		this.tipo = estrategia.getTipo();
		this.nomeRobo = estrategia.getAcao().getNomeRobo();
		this.data = LocalDateTime.now();
		this.valoracaoatual = estrategia.getAcao().getValoracaoatual();
		this.shortname = estrategia.getAcao().getShortname();
		this.level = estrategia.getAcao().getLevel();
		this.mudouLado = estrategia.getAcao().getMudouLado();
		this.status = estrategia.getStatus();
		this.loss = estrategia.getLoss();
		this.gain = estrategia.getGain();
		this.audit = estrategia.getAudit();
	}

}