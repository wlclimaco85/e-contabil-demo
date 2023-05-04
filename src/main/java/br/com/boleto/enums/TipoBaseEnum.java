package br.com.boleto.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import static java.util.Objects.isNull;

@Slf4j
@AllArgsConstructor
@Getter
public enum TipoBaseEnum {
	NAO_REGISTRADA(0), TESTE(1), TREINAMENTO(2), PRODUCAO(3);
	
	private Integer tipoBase;

	public static TipoBaseEnum valueOf(Integer idTipoBase) {
		for (TipoBaseEnum tipoBase : TipoBaseEnum.values()) {
			if (tipoBase.getTipoBase().equals(idTipoBase)) {
				return tipoBase;
			}
		}
		log.error("Tipo de base inválida. - Tipo Base: {} [Método: valueOf]", isNull(idTipoBase)? "Nulo" : idTipoBase.toString());
		throw new IllegalArgumentException("Tipo de base inválida");
	}
}
