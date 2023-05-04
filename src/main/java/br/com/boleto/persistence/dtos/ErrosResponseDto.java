package br.com.boleto.persistence.dtos;

import java.util.ArrayList;

import br.com.boleto.persistence.entity.Erros;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrosResponseDto {
	private ArrayList<Erros> erros;
	
	public ErrosResponseDto addArray(Erros estrategias) {
		ArrayList<Erros> estrategiass = new  ArrayList<Erros>();
		estrategiass.add(estrategias);
		this.setErros(estrategiass);
		return this;
	}
}
