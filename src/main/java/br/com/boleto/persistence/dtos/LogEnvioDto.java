package br.com.boleto.persistence.dtos;

import br.com.boleto.enums.SituacaoEnum;
import br.com.boleto.enums.StatusBoletoEnum;
import br.com.boleto.enums.TipoEventoEnum;
import br.com.boleto.persistence.entity.LogEnvio;
import br.com.boleto.util.DateUtil;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LogEnvioDto {
	
	private Integer id;
	private String nossonumero;
	private String dhocorrencia;
	private String mensagem;
	private String stacktrace;
	private Integer status;
	private String descricaoStatus;
	private Integer idTipoEvento;
	private String tipoEvento;
	private String descricaoTipoEvento;
	private Integer situacao;
	private String descricaoSituacao;
	private Integer statusBanco;
	
	public String getDescricaoStatus() {
		return StatusBoletoEnum.getDescricao(status);
	}
	
	public void setDescricaoStatus(StatusBoletoEnum descricaoStatus) {
		if (descricaoStatus != null) {
			this.descricaoStatus = descricaoStatus.getDescricao();
		}
	}
	
	public String getTipoEvento() {
		return TipoEventoEnum.getTipoEvento(idTipoEvento);
	}
	
	public void setTipoEvento(TipoEventoEnum tipoEvento) {
		if (tipoEvento != null) {
			this.tipoEvento = tipoEvento.getTipoEvento();
		}
	}
	
	public String getDescricaoTipoEvento() {
		return TipoEventoEnum.getDescricao(idTipoEvento);
	}
	
	public void setDescricaoTipoEvento(TipoEventoEnum descricaoTipoEvento) {
		if (descricaoTipoEvento != null) {
			this.descricaoTipoEvento = descricaoTipoEvento.getDescricaoEvento();
		}
	}
	
	public String getDescricaoSituacao() {
		return SituacaoEnum.getSituacao(situacao);
	}
	
	public void setDescricaoSituacao(SituacaoEnum descricaoSituacao) {
		if (descricaoSituacao != null) {
			this.descricaoSituacao = descricaoSituacao.getSituacao();
		}
	}
	
	public LogEnvioDto(LogEnvio logEnvio) {
		super();
		this.id = logEnvio.getId();
		this.nossonumero = logEnvio.getNossonumero();
		this.dhocorrencia = DateUtil.convertTimeStampToStringDth(logEnvio.getDhocorrencia());
		this.mensagem = logEnvio.getMensagem();
		this.stacktrace = logEnvio.getStacktrace();
		this.status = logEnvio.getStatus().getStatus();
		this.idTipoEvento = logEnvio.getTipoEvento().getIdTipoEvento();
		this.situacao = logEnvio.getSituacao().getIdSituacao();
		this.statusBanco = logEnvio.getStatusBanco().getStatus();
	}
	
	public LogEnvioDto(String nossonumero, String mensagem, String stacktrace, Integer status, Integer idTipoEvento, Integer situacao, Integer statusBanco) {
		super();
		this.nossonumero = nossonumero;
		this.mensagem = mensagem;
		this.stacktrace = stacktrace;
		this.status = status;
		this.idTipoEvento = idTipoEvento;
		this.situacao = situacao;
		this.statusBanco = statusBanco;
	}
}
