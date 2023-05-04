package br.com.boleto.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import static java.util.Objects.isNull;

@Slf4j
@AllArgsConstructor
@Getter
public enum SituacaoEnum {
	PROCESSADA(0, "Processada"),
	PENDENTE_ENVIO(1, "Pendente de Envio"),
	REJEITADA(2, "Rejeitada");
	
	private Integer idSituacao;

	private String situacao;
	
	public static SituacaoEnum valueOf(Integer idSituacao) {
		for (SituacaoEnum situacao : SituacaoEnum.values()) {
			if (situacao.getIdSituacao().equals(idSituacao)) {
				return situacao;
			}
		}
		log.error("Id de Situação inválido. - Id: {} [Método: valueOf]", isNull(idSituacao)? "Nulo" : idSituacao.toString());
		throw new IllegalArgumentException("Id de Situação inválido");
	}
	
	public static String getSituacao(Integer situacao) {
		for (SituacaoEnum situacaoEnum : SituacaoEnum.values()) {
			if (situacaoEnum.getIdSituacao().equals(situacao)) {
				return situacaoEnum.getSituacao();
			}
		}
		log.error("Situação inválida. - Id: {} [Método: getSituacao]", isNull(situacao)? "Nulo" : situacao.toString());
		throw new IllegalArgumentException("Situação inválida");
	}
}
