package br.com.boleto.persistence.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrdensResponseDto {
	private OrdensDto ordens;

	public OrdensResponseDto(OrdensDto ordens) {
		super();
		this.ordens = ordens;
	}
	
	public OrdensResponseDto() {
		super();
	}
	
}
