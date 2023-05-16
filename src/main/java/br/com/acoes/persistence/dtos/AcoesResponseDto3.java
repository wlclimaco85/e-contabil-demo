package br.com.acoes.persistence.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AcoesResponseDto3 {
	private Double valor;

	public AcoesResponseDto3() {
		super();
	}

	public AcoesResponseDto3(Double regularMarketPrice) {
		this.valor = regularMarketPrice;
	}
	
	
}
