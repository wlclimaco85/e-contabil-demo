package br.com.boleto.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import static java.util.Objects.isNull;

@Slf4j
@AllArgsConstructor
@Getter
public enum StatusOrdensAcoesEnum {
	INDICACOES(0, "Indicações", "Indicações"),
	NEGOCIADAS(1, "Negociadas", "Negociadas"),
	PROCESSADAS(2, "Processadas", "Processadas"),
	ERROS(3, "Erros", "Erros"),
	ERROS_CLOSE(4, "Erros Fechamento", "Erros Fechamento"),
	FECHADAS(5, "Fechadas", "Fechadas"),
	AGUARDANDO_FECHAMENTO(6, "Aguardando Fechamento", "Aguardando Fechamento");
	
	private Integer idTipoEvento;

	private String tipoEvento;

	private String descricaoEvento;
	
	public static StatusOrdensAcoesEnum valueOf(Integer idTipoEvento) {
		for (StatusOrdensAcoesEnum tipoEvento : StatusOrdensAcoesEnum.values()) {
			if (tipoEvento.getIdTipoEvento().equals(idTipoEvento)) {
				return tipoEvento;
			}
		}
		log.error("Id de Tipo Evento inválido. - Id: {} [Método: valueOf]", isNull(idTipoEvento)? "Nulo" : idTipoEvento.toString());
		throw new IllegalArgumentException("Id de Tipo Evento inválido");
	}
	
	public static String getTipoEvento(Integer idTipoEvento) {
		for (StatusOrdensAcoesEnum tipoEvento : StatusOrdensAcoesEnum.values()) {
			if (tipoEvento.getIdTipoEvento().equals(idTipoEvento)) {
				return tipoEvento.getTipoEvento();
			}
		}
		log.error("Id de Tipo Evento inválido. - Id: {} [Método: getTipoEvento]", isNull(idTipoEvento)? "Nulo" : idTipoEvento.toString());
		throw new IllegalArgumentException("Id de Tipo Evento inválido");
	}

	public static String getDescricao(Integer idTipoEvento) {
		for (StatusOrdensAcoesEnum tipoEvento : StatusOrdensAcoesEnum.values()) {
			if (tipoEvento.getIdTipoEvento().equals(idTipoEvento)) {
				return tipoEvento.getDescricaoEvento();
			}
		}
		log.error("Id de Tipo Evento inválido. - Id: {} [Método: getDescricao]", isNull(idTipoEvento)? "Nulo" : idTipoEvento.toString());
		throw new IllegalArgumentException("Id de Tipo Evento inválido");
	}
}
