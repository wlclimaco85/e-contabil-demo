package br.com.acoes.persistence.dtos;

import java.time.LocalDateTime;

import br.com.acoes.persistence.entity.Audit;
import br.com.acoes.persistence.entity.Ordens;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Acoes2Dto {
	private Integer id;
	private Integer corretoraId;
	private String corretoras;
	private Integer acaoId;
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
	
	public Acoes2Dto(Ordens ordens) {
		this.id = ordens.getId();
		this.corretoraId = ordens.getCorretora().getId();
		this.corretoras = ordens.getCorretora().getNome();
		this.acaoId = ordens.getAcao().getId();
		this.acao = ordens.getAcao().getAcao();
		this.status = ordens.getStatus();
		this.valorcompra = ordens.getValorsuj();
		this.loss = ordens.getLoss();
		this.gain = ordens.getGain();
		this.valorcomprado = ordens.getValorsuj();
		this.tipo = ordens.getTipo();
		this.contratos = ordens.getContratos();
		this.valorsuj = ordens.getValorsuj();
		this.valor = ordens.getValor();
	}
}