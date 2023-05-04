package br.com.boleto.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import static java.util.Objects.isNull;

@Slf4j
@AllArgsConstructor
@Getter
public enum TipoEventoEnum {
	EMISSAO(0, "Emissão", "Emissão de boleto enviada para o Banco"),
	ALTERACAO(1, "Alteração", "Alteração de boleto enviada para o Banco"),
	BAIXA_CANCELAMENTO(2, "Baixa/Cancelamento", "Baixa/cancelamento de boleto enviada para o Banco"),
	RETORNO_BANCO(3, "Retorno recebido do Banco", "Retorno recebido do Banco"),
	ERRO_VALIDACAO_BOLETO(4, "Erro ao validar boleto", "Emissão de boleto bloqueado pelo serviço");
	
	private Integer idTipoEvento;

	private String tipoEvento;

	private String descricaoEvento;
	
	public static TipoEventoEnum valueOf(Integer idTipoEvento) {
		for (TipoEventoEnum tipoEvento : TipoEventoEnum.values()) {
			if (tipoEvento.getIdTipoEvento().equals(idTipoEvento)) {
				return tipoEvento;
			}
		}
		log.error("Id de Tipo Evento inválido. - Id: {} [Método: valueOf]", isNull(idTipoEvento)? "Nulo" : idTipoEvento.toString());
		throw new IllegalArgumentException("Id de Tipo Evento inválido");
	}
	
	public static String getTipoEvento(Integer idTipoEvento) {
		for (TipoEventoEnum tipoEvento : TipoEventoEnum.values()) {
			if (tipoEvento.getIdTipoEvento().equals(idTipoEvento)) {
				return tipoEvento.getTipoEvento();
			}
		}
		log.error("Id de Tipo Evento inválido. - Id: {} [Método: getTipoEvento]", isNull(idTipoEvento)? "Nulo" : idTipoEvento.toString());
		throw new IllegalArgumentException("Id de Tipo Evento inválido");
	}

	public static String getDescricao(Integer idTipoEvento) {
		for (TipoEventoEnum tipoEvento : TipoEventoEnum.values()) {
			if (tipoEvento.getIdTipoEvento().equals(idTipoEvento)) {
				return tipoEvento.getDescricaoEvento();
			}
		}
		log.error("Id de Tipo Evento inválido. - Id: {} [Método: getDescricao]", isNull(idTipoEvento)? "Nulo" : idTipoEvento.toString());
		throw new IllegalArgumentException("Id de Tipo Evento inválido");
	}
}
