package br.com.boleto.persistence.dtos;

import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CorretoraResponseDto {
	private CorretoraDto corretora;
	
	public CorretoraResponseDto addArray(CorretoraDto estrategias) {
		ArrayList<CorretoraDto> estrategiass = new  ArrayList<CorretoraDto>();
		estrategiass.add(estrategias);
		return this;
	}
}
