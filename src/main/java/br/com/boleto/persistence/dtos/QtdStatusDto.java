package br.com.boleto.persistence.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QtdStatusDto {
	
	private Integer status;

	private Integer qtd;

	public QtdStatusDto(Integer status, Integer qtd) {
		super();
		this.status = status;
		this.qtd = qtd;
	}

	public QtdStatusDto() {
		super();
	}
	
	
}