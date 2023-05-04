package br.com.boleto.persistence.entity;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import br.com.boleto.enums.SituacaoEnum;
import br.com.boleto.enums.StatusBoletoEnum;
import br.com.boleto.enums.TipoEventoEnum;
import br.com.boleto.persistence.dtos.LogEnvioDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "logenvios", schema = "public")
@AllArgsConstructor
@NoArgsConstructor
public class LogEnvio {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String nossonumero;
	private Timestamp dhocorrencia;
	private String mensagem;
	private String stacktrace;
	private Integer status;
	private Integer tipoEvento;
	private Integer situacao;
	private Integer statusBanco;

	public StatusBoletoEnum getStatus() {
		return StatusBoletoEnum.valueOf(status);
	}

	public void setStatus(StatusBoletoEnum status) {
		if (status != null) {
			this.status = status.getStatus();
		}
	}

	public TipoEventoEnum getTipoEvento() {
		return TipoEventoEnum.valueOf(tipoEvento);
	}

	public void setTipoEvento(TipoEventoEnum tipoEvento) {
		if (tipoEvento != null) {
			this.tipoEvento = tipoEvento.getIdTipoEvento();
		}
	}

	public SituacaoEnum getSituacao() {
		return SituacaoEnum.valueOf(situacao);
	}

	public void setSituacao(SituacaoEnum situacao) {
		if (situacao != null) {
			this.situacao = situacao.getIdSituacao();
		}
	}

	public StatusBoletoEnum getStatusBanco() {
		return StatusBoletoEnum.valueOf(statusBanco);
	}

	public void setStatusBanco(StatusBoletoEnum statusBanco) {
		if (statusBanco != null) {
			this.statusBanco = statusBanco.getStatus();
		}
	}
	
	public void setlogEnvio(LogEnvioDto logEnvio) {
		this.nossonumero = logEnvio.getNossonumero();

		TimeZone tz = TimeZone.getTimeZone("America/Sao_Paulo");
		TimeZone.setDefault(tz);
		Calendar ca = GregorianCalendar.getInstance(tz);
		
		Timestamp timestamp = new Timestamp(ca.getTime().getTime());
		this.dhocorrencia = timestamp;
		this.mensagem = logEnvio.getMensagem();
		this.stacktrace = logEnvio.getStacktrace();
		this.status = logEnvio.getStatus();
		this.tipoEvento = logEnvio.getIdTipoEvento();
		this.situacao = logEnvio.getSituacao();
		this.statusBanco = logEnvio.getStatusBanco();
	}


	public LogEnvio(String nossonumero, String mensagem, String stacktrace, Integer status, Integer tipoEvento,
			Integer situacao, Integer statusBanco) {
		super();
		TimeZone tz = TimeZone.getTimeZone("America/Sao_Paulo");
		TimeZone.setDefault(tz);
		Calendar ca = GregorianCalendar.getInstance(tz);
		
		Timestamp timestamp = new Timestamp(ca.getTime().getTime());
		this.dhocorrencia = timestamp;
		this.nossonumero = nossonumero;
		this.mensagem = mensagem;
		this.stacktrace = stacktrace;
		this.status = status;
		this.tipoEvento = tipoEvento;
		this.situacao = situacao;
		this.statusBanco = statusBanco;
	}
}
