package br.com.acoes.persistence.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EstrategiasResponseDto2 {
	private EstrategiasDto estrategias;

	public EstrategiasResponseDto2(EstrategiasDto estrategias) {
		super();
		this.estrategias = estrategias;
	}

	public EstrategiasResponseDto2() {
		super();
	}
	
	
	
}
