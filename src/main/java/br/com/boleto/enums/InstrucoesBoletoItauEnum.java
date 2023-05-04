package br.com.boleto.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum InstrucoesBoletoItauEnum {
	PROTESTAR(1, "Protestar"),
	NEGATIVAR(2, "Negativar"),
	NAO_PROTESTAR(4, "Não protestar"),
	NAO_NEGATIVAR(5, "Não negativar"),
	NAO_RECEBER_APOS_VENCIMENTO(7, "Não receber após XX de vencimento"),
	CANCELAR_APOS_VENCIMENTO(8, "Cancelar (Baixar/Devolver) após XX de vencimento");
	
	private Integer codigo;
	private String instrucao;
	
	public static InstrucoesBoletoItauEnum valueOf(Integer codigo) {
		if (codigo == null) {
			return null;
		}
		for (InstrucoesBoletoItauEnum instrucaoBoleto : InstrucoesBoletoItauEnum.values()) {
			if (instrucaoBoleto.getCodigo().equals(codigo)) {
				return instrucaoBoleto;
			}
		}
		throw new IllegalArgumentException("Código inválido: " + codigo);
	}
}
